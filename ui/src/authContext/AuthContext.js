import { createContext, useState, useEffect, useContext, useCallback } from 'react';
import { useNavigate } from 'react-router-dom'; // Оставил, если вам нужна навигация внутри контекста
import { login as authLogin, register as authRegister } from '../services/AuthService'; // Проверьте путь
import { jwtDecode } from 'jwt-decode'; 

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token') || null);
    const [loading, setLoading] = useState(true); 
    //const navigate = useNavigate(); 

    // useCallback, чтобы функция не пересоздавалась на каждом рендере
    const decodeAndSetUser = useCallback((jwtToken) => {
        if (jwtToken) {
            try {
                const decoded = jwtDecode(jwtToken);
                if (decoded && decoded.email && decoded.role) {
                    setUser({
                        email: decoded.email,
                        role: decoded.role // 'ROLE_USER' или 'ROLE_ADMIN'
                    });
                } else {
                    console.warn("JWT токен декодирован, но отсутствуют ожидаемые поля 'email' или 'role'.");
                    // Token not valid without nesseccary fields
                    setToken(null);
                    localStorage.removeItem('token');
                    setUser(null);
                }
            } catch (error) {
                console.error("Не удалось декодировать токен или токен невалиден:", error);
                // Token expired or corrupted - remove it
                setToken(null);
                localStorage.removeItem('token');
                setUser(null);
            }
        } else {
            setUser(null); // No token -> no user
        }
    }, []);

    // Хук запускается при изменении токена или при монтировании компонента
    useEffect(() => {
        if (token) {
            localStorage.setItem('token', token);
            decodeAndSetUser(token); 
          } else {
              localStorage.removeItem('token');
              setUser(null); 
          }
          setLoading(false); 
      }, [token, decodeAndSetUser]); 
  
      const login = async (email, password) => {
          try {
              const response = await authLogin(email, password);
              const newToken = response.data.token; 
              setToken(newToken); 
              // navigate('/'); 
              return { success: true };
          } catch (error) {
              console.error('Ошибка входа:', error);
              // In case of a login error, you can also clear the token if it was
              // setToken(null);
              return { success: false, error: error.message };
          }
      };
  
      const register = async (email, password, loginName) => { 
          try {
              const response = await authRegister(email, password, loginName);
              const newToken = response.data.token;
              setToken(newToken); 
              // navigate('/'); 
              return { success: true };
          } catch (error) {
              console.error('Ошибка регистрации:', error);
              // setToken(null);
              return { success: false, error: error.message };
          }
      };
  
      const logout = () => {
          setToken(null); // Will update the 'token' state to null and call 'useEffect'
          // navigate('/'); 
          return { success: true };
      };
  
      return (
          <AuthContext.Provider value={{ user, token, login, register, logout, loading }}>
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