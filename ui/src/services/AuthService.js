import axios from 'axios';

//const AUTH_REST_API_BASE_URL = "http://localhost:8080/auth";
const AUTH_REST_API_BASE_URL = "/auth";

export const login = (email, password) => {
    return axios.post(`${AUTH_REST_API_BASE_URL}/login`, { email, password });
};

export const register = (email, password, login) => {
    return axios.post(`${AUTH_REST_API_BASE_URL}/register`, { email, password, login });
};

//The Interceptor to auto adding a token
axios.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
}, (error) => {
    return Promise.reject(error);
});

//Adding Handler for 401 error (Not authorized)
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 401) {
            localStorage.remove('token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);