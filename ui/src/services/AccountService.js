import axios from 'axios';

const ACCOUNT_REST_API_BASE_URL = "http://localhost:8088/api/v1/accounts"

export const accountList = () => axios.get(ACCOUNT_REST_API_BASE_URL);
