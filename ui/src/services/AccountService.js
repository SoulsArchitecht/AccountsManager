import axios from 'axios';

const ACCOUNT_REST_API_BASE_URL = "http://localhost:8088/account";

export const accountList = () => axios.get(ACCOUNT_REST_API_BASE_URL);

export const createAccount = (account) => axios.post(ACCOUNT_REST_API_BASE_URL, account);

export const getAccount = (accountId) => axios.get(ACCOUNT_REST_API_BASE_URL + '/' + accountId);

export const updateAccount = (accountId, account) => axios.put(ACCOUNT_REST_API_BASE_URL + '/' + accountId, account);

export const deleteAccount = (accountId) => axios.delete(ACCOUNT_REST_API_BASE_URL + '/' + accountId);