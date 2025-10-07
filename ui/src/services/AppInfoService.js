import axios from 'axios';

const API_BASE_URL = '';

export const fetchAppInfo = async () => {
    const response = await axios.get(`${API_BASE_URL}/app-info`);
    return response.data;
};
