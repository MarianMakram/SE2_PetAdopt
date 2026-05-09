import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import apiClient from '../services/apiClient';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user');
    try {
      return saved ? JSON.parse(saved) : null;
    } catch {
      return null;
    }
  });
  const [isLoading, setIsLoading] = useState(true);

  // Validate session on mount by calling /auth/me
  // If the endpoint doesn't exist yet, fall back to the stored user
  const validateSession = useCallback(async () => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      setUser(null);
      setIsLoading(false);
      return;
    }

    try {
      const response = await apiClient.get('/auth/me');
      // Spring Boot returns the user data directly (no wrapper)
      const userData = response.data;
      if (userData) {
        // Normalize the user object shape for the frontend
        const normalizedUser = {
          id: userData.userId || userData.id,
          email: userData.email,
          firstName: userData.firstName,
          lastName: userData.lastName,
          role: userData.role, // ADMIN, SHELTER, or ADOPTER
          phone: userData.phone,
          city: userData.city,
          country: userData.country,
        };
        setUser(normalizedUser);
        localStorage.setItem('user', JSON.stringify(normalizedUser));
      }
    } catch (err) {
      console.warn("Session validation failed:", err.message);
      // If /auth/me returns 404 (not implemented yet), keep stored user
      if (err.response?.status === 404) {
        // Endpoint not available — keep existing user from localStorage
        console.info("Auth /me endpoint not available, using cached user data.");
      } else {
        // Token is truly invalid — clear everything
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        setUser(null);
      }
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    validateSession();
  }, [validateSession]);

  // Login — calls POST /auth/login and stores the JWT + user info
  const login = async (email, password) => {
    const response = await apiClient.post('/auth/login', { email, password });
    // Spring Boot AuthResponse: { accessToken, refreshToken, userId, email, firstName, lastName, role }
    const data = response.data;

    if (!data?.accessToken) {
      throw new Error("Login failed: No access token received from server.");
    }

    const { accessToken, refreshToken } = data;

    // Build a user object from the AuthResponse fields
    const userData = {
      id: data.userId,
      email: data.email,
      firstName: data.firstName,
      lastName: data.lastName,
      role: data.role, // ADMIN, SHELTER, or ADOPTER
      phone: data.phone,
      city: data.city,
      country: data.country,
      accountStatus: data.accountStatus,
    };

    localStorage.setItem('accessToken', accessToken);
    if (refreshToken) localStorage.setItem('refreshToken', refreshToken);
    localStorage.setItem('user', JSON.stringify(userData));

    setUser(userData);
    return userData;
  };

  // Logout — clears tokens and user state
  const logout = async () => {
    try {
      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken) {
        await apiClient.post('/auth/logout', { refreshToken });
      }
    } catch (err) {
      // Logout API may not exist — that's okay, we clear locally regardless
      console.warn("Logout API call failed (may not be implemented):", err.message);
    } finally {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      setUser(null);
    }
  };

  return (
    <AuthContext.Provider value={{ user, isLoading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
