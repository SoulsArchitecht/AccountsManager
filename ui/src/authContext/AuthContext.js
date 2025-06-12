import {createContext, useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { login as authLogin, register as authRegister } from '../../src/services/AuthService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token') || null);
    //const navigate = useNavigate();

    useEffect(() => {
        if (token) {
            // Здесь можно добавить запрос для получения данных пользователя
            // пример: fetchUserData().then(user => setUser(user));
            localStorage.setItem('token', token);
        } else {
            localStorage.removeItem('token');
            setUser(null);
        }
    }, [token]);

    const login = async (email, password) => {
        try {
          const response = await authLogin(email, password);
          setToken(response.data.token);
          return { success: true };
          //navigate('/');
        } catch (error) {
          console.error('Login error:', error);
          return { success: false, error: error.message }
          //throw error;
        }
      };
    
      const register = async (email, password, login) => {
        try {
          const response = await authRegister(email, password, login);
          setToken(response.data.token);
          return { success: true };
          //navigate('/');
        } catch (error) {
          console.error('Registration error:', error);
          return { success: false, error: error.message };
          //throw error;
        }
      };

    const logout = () => {
        setToken(null);
        return { success: true };
        //navigate('/');
    };

    return (
        <AuthContext.Provider value = {{ user, token, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};