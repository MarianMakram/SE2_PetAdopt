import React, { createContext, useContext, useEffect, useState, useCallback } from 'react';
import { useAuth } from './AuthContext';
import apiClient from '../services/apiClient';

const NotificationContext = createContext();

export const useNotifications = () => useContext(NotificationContext);

// Polling interval for checking new notifications (30 seconds)
const POLL_INTERVAL_MS = 30000;

export const NotificationProvider = ({ children }) => {
  const { user } = useAuth();
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);

  // Fetch notifications from the Interaction Service via API Gateway
  // Endpoint: GET /api/notifications/user/{userId}
  const fetchNotifications = useCallback(async () => {
    if (!user?.id) return;
    try {
      const response = await apiClient.get(`/notifications/user/${user.id}`);
      const data = response.data || [];
      setNotifications(data);
      // Spring Boot Notification model uses 'isRead' (camelCase from Java boolean)
      // Jackson serializes boolean 'isRead' as 'read' in JSON
      setUnreadCount(data.filter(n => !n.read && !n.isRead).length);
    } catch (error) {
      // Notifications service may not be available yet — fail silently
      if (error.response?.status !== 404) {
        console.error('Error fetching notifications:', error);
      }
    }
  }, [user?.id]);

  // Fetch on mount and when user changes
  useEffect(() => {
    if (user) {
      fetchNotifications();
    } else {
      setNotifications([]);
      setUnreadCount(0);
    }
  }, [user, fetchNotifications]);

  // Poll for new notifications every POLL_INTERVAL_MS
  // This replaces the old SignalR real-time connection
  useEffect(() => {
    if (!user?.id) return;

    const intervalId = setInterval(fetchNotifications, POLL_INTERVAL_MS);
    return () => clearInterval(intervalId);
  }, [user?.id, fetchNotifications]);

  // Mark a single notification as read
  // Endpoint: PATCH /api/notifications/{id}/read
  const markAsRead = async (id) => {
    try {
      await apiClient.patch(`/notifications/${id}/read`);
      setNotifications(prev =>
        prev.map(n => n.id === id ? { ...n, read: true, isRead: true } : n)
      );
      setUnreadCount(prev => Math.max(0, prev - 1));
    } catch (error) {
      console.error('Error marking notification as read:', error);
    }
  };

  // Mark all notifications as read (client-side batch — backend doesn't have bulk endpoint)
  const markAllAsRead = async () => {
    try {
      // Mark each unread notification individually
      const unreadNotifs = notifications.filter(n => !n.read && !n.isRead);
      await Promise.all(
        unreadNotifs.map(n => apiClient.patch(`/notifications/${n.id}/read`))
      );
      setNotifications(prev => prev.map(n => ({ ...n, read: true, isRead: true })));
      setUnreadCount(0);
    } catch (error) {
      console.error('Error marking all as read:', error);
    }
  };

  return (
    <NotificationContext.Provider value={{
      notifications,
      unreadCount,
      markAsRead,
      markAllAsRead,
      fetchNotifications
    }}>
      {children}
    </NotificationContext.Provider>
  );
};
