import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { getAllAccounts, deleteAccount, toggleActive } from '../../services/AccountService';
import { useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { useTable } from 'react-table';
import Pagination from '@mui/material/Pagination';
import { useAuth } from '../../authContext/AuthContext';
import '../accountList/AccountList.css';
import { useLocalization } from '../../context/LocalizationContext';

const AccountList = () => {
    const [data, setData] = useState({
        accounts: [],
        totalPages: 0,
        totalElements: 0,
        loading: true,
        error: null,
        success: null
    });
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(5);
    const [keyword, setKeyword] = useState('');
    const [activeFilter, setActiveFilter] = useState(null); // null - все, true - активные, false - неактивные
    const [sortField, setSortField] = useState('createdAt');
    const [sortDirection, setSortDirection] = useState('desc');
    const { token } = useAuth();
    const navigate = useNavigate();
    const { t } = useLocalization();

    const fetchAccounts = useCallback(async () => {
        try {
            setData(prev => ({ ...prev, loading: true, error: null, success: null }));
            
            const params = {
                page,
                size: pageSize,
                keyword: keyword || undefined,
                active: activeFilter,
                sort: `${sortField},${sortDirection}`
            };
            
            Object.keys(params).forEach(key => params[key] === undefined && delete params[key]);
            
            const response = await getAllAccounts(params);
            setData({
                accounts: response.data.content || [],
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                loading: false,
                error: null,
                success: null
            });
        } catch (error) {
            setData({
                accounts: [],
                totalPages: 0,
                totalElements: 0,
                loading: false,
                error: error.response?.data?.message || t('error.loading_accounts'),
                success: null
            });
            console.error('Error fetching accounts:', error);
        }
    }, [page, pageSize, keyword, activeFilter, sortField, sortDirection, token]);

    useEffect(() => {
        const timer = setTimeout(() => {
            fetchAccounts();
        }, 500); // Задержка для debounce при вводе текста
        
        return () => clearTimeout(timer);
    }, [fetchAccounts]);

    const handleStatusFilterChange = (value) => {
        setActiveFilter(value);
        setPage(0);
    };

    const removeAccount = async (id) => {
        if (window.confirm(t('help.delete_confirm'))) {
            deleteAccount(id)
                .then(() => {
                    setData(prev => ({
                        ...prev,
                        error: null,
                        success: t('help.delete_successfully')
                    }));
                    fetchAccounts();
                })
                .catch(error => {
                    console.error('Delete error: ', error);
                    setData(prev => ({
                        ...prev,
                        error: error.response?.data?.message || 'Failed to delete account',
                        success: null
                    }));
                });
        };
    };

    const toggleAccountStatus = async (id, currentStatus) => {
        const actionKey = currentStatus ? 'toggle.deactivate' : 'toggle.activate';
        const actionVerb = currentStatus ? 'deactivate' : 'activate';
        if (!window.confirm(`Are you sure you want to ${t(actionKey).toLowerCase()} this account?`)) {
            return;
        }
        
        try {
            await toggleActive(id);
            setData(prev => ({
                ...prev,
                error: null,
                success: `Account ${t(actionVerb === 'activate' ? 'toggle.activate' : 'toggle.deactivate').toLowerCase()}d successfully!`
            }));
            fetchAccounts();
        } catch (error) {
            console.error('Status change error:', error);
            setData(prev => ({
                ...prev,
                error: error.response?.data?.message || `Failed to ${actionVerb} account`,
                success: null
            }));
        }
    };

    const handleSort = (field) => {
        if (sortField === field) {
            setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
        } else {
            setSortField(field);
            setSortDirection('asc');
        }
        setPage(0);
    };

    const columns = useMemo(() => [
        { 
            Header: t('table.account.link'), 
            accessor: 'link',
            Cell: ({ value }) => <a href={value} target="_blank" rel="noopener noreferrer">{value}</a>
        },
        { 
            Header: t('table.account.description'), 
            accessor: 'description',
            Cell: ({ value }) => value || '-'
        },
        { 
            Header: () => (
                <span 
                    className="sortable-header"
                    onClick={() => handleSort('createdAt')}
                >
                    {t('table.account.created_at')} {sortField === 'createdAt' && (sortDirection === 'asc' ? '↑' : '↓')}
                </span>
            ), 
            accessor: 'createdAt',
            Cell: ({ value }) => format(new Date(value), 'dd.MM.yyyy HH:mm')
        },
        { 
            Header: () => (
                <span 
                    className="sortable-header"
                    onClick={() => handleSort('changedAt')}
                >
                    {t('table.account.updated_at')} {sortField === 'changedAt' && (sortDirection === 'asc' ? '↑' : '↓')}
                </span>
            ), 
            accessor: 'changedAt',
            Cell: ({ value }) => format(new Date(value), 'dd.MM.yyyy HH:mm')
        },
        { 
            Header: () => (
                <span 
                    className="sortable-header"
                    onClick={() => handleSort('login')}
                >
                    {t('table.account.login')} {sortField === 'login' && (sortDirection === 'asc' ? '↑' : '↓')}
                </span>
            ), 
            accessor: 'login' 
        },
        { 
            Header: t('table.account.email'), 
            accessor: 'email',
            Cell: ({ value }) => <a href={`mailto:${value}`}>{value}</a>
        },
        { 
            Header: t('table.account.password'), 
            accessor: 'password',
            Cell: ({ value }) => value || '-'
        },
        { 
            Header: t('table.account.status'), 
            accessor: 'active',
            Cell: ({ value }) => (
                <span className={`badge ${value ? 'bg-success' : 'bg-secondary'}`}>
                    {value ? t('common.status_active') : t('common.status_inactive')}
                </span>
            )
        },
        {
            Header: t('table.account.actions'),
            accessor: 'actions',
            Cell: ({ row }) => (
                <div className="actions-container">
                    <button 
                        onClick={() => navigate(`/edit-account/${row.original.id}`)}
                        className="action-btn btn-edit"
                    >
                        {t('button.edit')}
                    </button>
                    <button 
                        onClick={() => toggleAccountStatus(row.original.id, row.original.active)}
                        className={`action-btn ${row.original.active ? 'btn-deactivate' : 'btn-activate'}`}
                    >
                        {row.original.active ? t('toggle.deactivate') : t('toggle.activate')}
                    </button>
                    <button
                        onClick={() => removeAccount(row.original.id)}
                        className="action-btn btn-delete"
                    >
                        {t('button.delete')}
                    </button>    
                </div>
            )
        }
    ], [sortField, sortDirection]);

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
    } = useTable({ columns, data: data.accounts });

    if (data.loading) return <div className="text-center mt-4">{t('common.loading')}</div>;
    if (data.error) return <div className="alert alert-danger">{data.error}</div>;

    return (
        <div className="container mt-4">
            <div className="card">
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h3>{t('common.accounts_list')}</h3>
                    <button 
                        onClick={() => navigate('/add-account')}
                        className="btn btn-primary"
                    >
                        {t('button.add_account')}
                    </button>
                </div>
                
                <div className="card-body">
                    {data.success && (
                        <div className="alert alert-success">{data.success}</div>
                    )}
                    {data.error && (
                        <div className="alert alert-danger">{data.error}</div>
                    )}

                    <div className="row mb-3">
                        <div className="col-md-6">
                            <div className="input-group">
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder={t('help.search_panel')}
                                    value={keyword}
                                    onChange={(e) => setKeyword(e.target.value)}
                                    onKeyDown={(e) => e.key === 'Enter' && fetchAccounts()}
                                />
                                <button 
                                    className="btn btn-outline-secondary"
                                    onClick={() => {
                                        setPage(0);
                                        fetchAccounts();
                                    }}
                                >
                                    {t('button.search')}
                                </button>
                            </div>
                        </div>
                        <div className="col-md-6">
                            <div className="d-flex align-items-center justify-content-end">
                                <div className="btn-group" role="group">
                                    <button
                                        type="button"
                                        className={`btn ${activeFilter === null ? 'btn-primary' : 'btn-outline-primary'}`}
                                        onClick={() => handleStatusFilterChange(null)}
                                    >
                                        {t('toggle.all')}
                                    </button>
                                    <button
                                        type="button"
                                        className={`btn ${activeFilter === true ? 'btn-primary' : 'btn-outline-primary'}`}
                                        onClick={() => handleStatusFilterChange(true)}
                                    >
                                        {t('toggle.active')}
                                    </button>
                                    <button
                                        type="button"
                                        className={`btn ${activeFilter === false ? 'btn-primary' : 'btn-outline-primary'}`}
                                        onClick={() => handleStatusFilterChange(false)}
                                    >
                                        {t('toggle.inactive')}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="table-responsive">
                        <table {...getTableProps()} className="table table-striped">
                            <thead>
                                {headerGroups.map(headerGroup => (
                                    <tr {...headerGroup.getHeaderGroupProps()}>
                                        {headerGroup.headers.map(column => (
                                            <th {...column.getHeaderProps()}>
                                                {column.render('Header')}
                                            </th>
                                        ))}
                                    </tr>
                                ))}
                            </thead>
                            <tbody {...getTableBodyProps()}>
                                {rows.map(row => {
                                    prepareRow(row);
                                    return (
                                        <tr {...row.getRowProps()}>
                                            {row.cells.map(cell => (
                                                <td {...cell.getCellProps()}>
                                                    {cell.render('Cell')}
                                                </td>
                                            ))}
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    </div>

                    <div className="d-flex justify-content-between align-items-center mt-3">
                        <div>
                            <select 
                                className="form-select"
                                value={pageSize}
                                onChange={(e) => {
                                    setPageSize(Number(e.target.value));
                                    setPage(0);
                                }}
                            >
                                {[5, 10, 20, 50].map(size => (
                                    <option key={size} value={size}>{size} {t('common.per_page')}</option>
                                ))}
                            </select>
                        </div>
                        
                        <Pagination
                            count={data.totalPages}
                            page={page + 1}
                            onChange={(_, newPage) => setPage(newPage - 1)}
                            color="primary"
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AccountList;