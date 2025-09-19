import axios from 'axios';

const ACCOUNT_REST_API_BASE_URL = "http://localhost:8080/accounts";

export const getAllAccounts = (params) => {
    return axios.get(ACCOUNT_REST_API_BASE_URL, {
        params: {
            ...params,
            keyword: params.keyword || '%',
            active: params.active
        }});
};        

export const createAccount = (account) => axios.post(ACCOUNT_REST_API_BASE_URL, account);

export const getAccount = (accountId) => axios.get(ACCOUNT_REST_API_BASE_URL + '/' + accountId);

export const updateAccount = (accountId, account) => axios.put(ACCOUNT_REST_API_BASE_URL + '/' + accountId, account);

export const deleteAccount = (accountId) => axios.delete(ACCOUNT_REST_API_BASE_URL + '/' + accountId);

export const findByKeyword = (keyword) => axios.get(ACCOUNT_REST_API_BASE_URL + '/search/' + keyword);

export const toggleActive = (accountId) => axios.patch(ACCOUNT_REST_API_BASE_URL + '/' + accountId + '/activate');