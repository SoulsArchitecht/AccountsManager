import { createContext, useState, useEffect, useContext, useCallback } from 'react';
import { login as authLogin, register as authRegister } from '../services/AuthService';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';
import {
  updateUserInfo as serviceUpdateUserInfo,
  uploadAvatar as serviceUploadAvatar,
  getUserInfo as serviceGetUserInfo
} from '../services/UserService';

const USER_REST_API_BASE_URL = "http://localhost:8088/users";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [loading, setLoading] = useState(true);

  const decodeAndSetUser = useCallback(async (jwtToken) => {
    if (jwtToken) {
      try {
        const decoded = jwtDecode(jwtToken);
        if (decoded && decoded.email && decoded.role) {
          const userInfoResponse = await serviceGetUserInfo(jwtToken);
          setUser({
            email: decoded.email,
            role: decoded.role,
            userInfo: userInfoResponse.data
          });
        } else {
          console.warn("JWT токен декодирован, но отсутствуют ожидаемые поля 'email' или 'role'.");
          setToken(null);
          localStorage.removeItem('token');
          setUser(null);
        }
      } catch (error) {
        console.error("Не удалось декодировать токен или токен невалиден:", error);
        setToken(null);
        localStorage.removeItem('token');
        setUser(null);
      }
    } else {
      setUser(null);
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    if (token) {
      localStorage.setItem('token', token);
      decodeAndSetUser(token);
    } else {
      localStorage.removeItem('token');
      setUser(null);
      setLoading(false);
    }
  }, [token, decodeAndSetUser]);

  const login = async (email, password) => {
    try {
      const response = await authLogin(email, password);
      const newToken = response.data.token;
      setToken(newToken);
      const userInfoResponse = await serviceGetUserInfo(newToken);
      const decoded = jwtDecode(newToken);
      setUser({
        email: decoded.email,
        role: decoded.role,
        userInfo: userInfoResponse.data
      });
      return { success: true };
    } catch (error) {
      console.error('Ошибка входа:', error);
      return { success: false, error: error.message };
    }
  };

  const updateUserInfo = async (newUserInfo) => {
    try {
      const response = await serviceUpdateUserInfo({
        ...newUserInfo,
        birthDate: newUserInfo.birthDate?.split('T')[0] // формат YYYY-MM-DD
      });
      setUser(prev => ({
        ...prev,
        userInfo: response.data
      }));
      return { success: true };
    } catch (error) {
      console.error('Failed to update user info:', error);
      return { success: false, error: error.response?.data?.message || 'Update failed' };
    }
  };

  const updateAvatar = async (file) => {
    try {
      const response = await serviceUploadAvatar(file);
      setUser(prev => ({
        ...prev,
        userInfo: {
          ...prev.userInfo,
          avatarUrl: response.data
        }
      }));
      return { success: true };
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.message || 'Upload failed'
      };
    }
  };

  const register = async (email, password, loginName) => {
    try {
      const response = await authRegister(email, password, loginName);
      const newToken = response.data.token;
      setToken(newToken);
      return { success: true };
    } catch (error) {
      console.error('Ошибка регистрации:', error);
      return { success: false, error: error.message };
    }
  };

  const logout = () => {
    setToken(null);
    return { success: true };
  };

  return (
    <AuthContext.Provider value={{ user, token, login, register, logout, loading, updateUserInfo, updateAvatar }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth должен использоваться внутри AuthProvider');
  }
  return context;
};