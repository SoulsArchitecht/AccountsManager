import axios from 'axios';

const USER_REST_API_BASE_URL = "http://localhost:8088/users";

//export const getAllUsers = (params) => axios.get(USER_REST_API_BASE_URL, {params});

export const getAllUsers = (params) => {
    return axios.get(USER_REST_API_BASE_URL, {
        params: {
            ...params,
            keyword: params.keyword || '%',
            status: params.status
    }});
}; 

export const createUser = (user) => axios.post(USER_REST_API_BASE_URL, user);

export const getUser = (userId) => axios.get(USER_REST_API_BASE_URL + '/' + userId);

export const updateUser = (userId, user) => axios.put(USER_REST_API_BASE_URL + '/' + userId, user);

export const deleteUser = (userId) => axios.delete(USER_REST_API_BASE_URL + '/' + userId);

export const findByKeyword = (keyword) => axios.get(USER_REST_API_BASE_URL + '/search/' + keyword);

export const toggleStatus = (userId) => axios.patch(USER_REST_API_BASE_URL + '/' + userId + '/activate');

export const getUserInfo = () => axios.get(USER_REST_API_BASE_URL + '/info/me');

export const updateUserInfo = (userInfo) => axios.put(USER_REST_API_BASE_URL + '/info/me', userInfo, {
    headers: {
        'Content-Type': 'application/json'
    }
});

export const uploadAvatar = (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return axios.post(USER_REST_API_BASE_URL + '/info/me/avatar', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        transformRequest: (data, headers) => {
            delete headers['Content-Type'];
            return data;
        }
    });
};