import axios from 'axios';

// API Gateway is the single entry point for all backend microservices
const BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor — attach JWT Bearer token to every request
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor — handle 401 (token expired) and normalize Spring Boot errors
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // If 401 and we haven't retried, attempt token refresh
    if (error.response?.status === 401 && !originalRequest._retry && originalRequest.url !== '/auth/login') {
      originalRequest._retry = true;
      try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) throw new Error("No refresh token available");

        const response = await axios.post(`${BASE_URL}/auth/refresh`, { refreshToken });

        // Spring Boot returns the AuthResponse directly (no wrapper)
        const data = response.data;
        const newAccessToken = data?.accessToken;
        const newRefreshToken = data?.refreshToken;

        if (newAccessToken) {
          localStorage.setItem('accessToken', newAccessToken);
          if (newRefreshToken) localStorage.setItem('refreshToken', newRefreshToken);

          originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
          return apiClient(originalRequest); // retry original request
        }
      } catch (refreshError) {
        // Refresh failed — clear auth state and let the UI redirect to login
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        return Promise.reject(refreshError);
      }
    }

    // Normalize Spring Boot error response for consistent UI handling
    // Spring Boot returns: { timestamp, status, error, message, path }
    if (error.response?.data) {
      const springError = error.response.data;
      // If Spring Boot returned a structured error with a message field, surface it
      if (springError.message && typeof springError.message === 'string') {
        error.message = springError.message;
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;
