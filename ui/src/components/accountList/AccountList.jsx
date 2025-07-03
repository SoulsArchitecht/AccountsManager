import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { getAllAccounts, deleteAccount, toggleActive } from '../../services/AccountService';
import { useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { useTable } from 'react-table';
import Pagination from '@material-ui/lab/Pagination';
import { useAuth } from '../../authContext/AuthContext';
import '../accountList/AccountList.css';

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
            
            // Удаляем undefined параметры
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
                error: error.response?.data?.message || 'Failed to fetch accounts',
                success: null
            });
            console.error('Error fetching accounts:', error);
        }
    }, [page, pageSize, keyword, activeFilter, sortField, sortDirection, token]);

    useEffect(() => {
        const timer = setTimeout(() => {
            fetchAccounts();
        }, 1000); // Задержка для debounce при вводе текста
        
        return () => clearTimeout(timer);
    }, [fetchAccounts]);

    const handleStatusFilterChange = (value) => {
        setActiveFilter(value);
        setPage(0);
    };

    const removeAccount = async (id) => {
        if (window.confirm(`Are you sure you to delete this account?`)) {
            deleteAccount(id)
                .then(() => {
                    setData(prev => ({
                        ...prev,
                        error: null,
                        success: 'Account deleted successfully'
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
        const action = currentStatus ? 'deactivate' : 'activate';
        if (!window.confirm(`Are you sure you want to ${action} this account?`)) {
            return;
        }
        
        try {
            await toggleActive(id);
            setData(prev => ({
                ...prev,
                error: null,
                success: `Account ${action}d successfully!`
            }));
            fetchAccounts();
        } catch (error) {
            console.error('Status change error:', error);
            setData(prev => ({
                ...prev,
                error: error.response?.data?.message || `Failed to ${action} account`,
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
            Header: 'Link', 
            accessor: 'link',
            Cell: ({ value }) => <a href={value} target="_blank" rel="noopener noreferrer">{value}</a>
        },
        { 
            Header: 'Description', 
            accessor: 'description',
            Cell: ({ value }) => value || '-'
        },
        { 
            Header: () => (
                <span 
                    className="sortable-header"
                    onClick={() => handleSort('createdAt')}
                >
                    Created At {sortField === 'createdAt' && (sortDirection === 'asc' ? '↑' : '↓')}
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
                    Updated At {sortField === 'changedAt' && (sortDirection === 'asc' ? '↑' : '↓')}
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
                    Login {sortField === 'login' && (sortDirection === 'asc' ? '↑' : '↓')}
                </span>
            ), 
            accessor: 'login' 
        },
        { 
            Header: 'Email', 
            accessor: 'email',
            Cell: ({ value }) => <a href={`mailto:${value}`}>{value}</a>
        },
        { 
            Header: 'Status', 
            accessor: 'active',
            Cell: ({ value }) => (
                <span className={`badge ${value ? 'bg-success' : 'bg-secondary'}`}>
                    {value ? 'Active' : 'Inactive'}
                </span>
            )
        },
        {
            Header: 'Actions',
            accessor: 'actions',
            Cell: ({ row }) => (
                <div>
                    <button 
                        onClick={() => navigate(`/edit-account/${row.original.id}`)}
                        className="btn btn-warning btn-sm me-2"
                    >
                        Edit
                    </button>
                    <button 
                        onClick={() => toggleAccountStatus(row.original.id, row.original.active)}
                        className={`btn btn-sm ${row.original.active ? 'btn-danger' : 'btn-success'}`}
                    >
                        {row.original.active ? 'Deactivate' : 'Activate'}
                    </button>
                    <button
                        onClick={() => removeAccount(row.original.id)}
                        className="btn btn-danger btn-sm"
                    >
                        Delete
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

    if (data.loading) return <div className="text-center mt-4">Loading...</div>;
    if (data.error) return <div className="alert alert-danger">{data.error}</div>;

    return (
        <div className="container mt-4">
            <div className="card">
                <div className="card-header d-flex justify-content-between align-items-center margin-left: 40">
                    <h3>Accounts Management</h3>
                    <button 
                        onClick={() => navigate('/add-account')}
                        className="btn btn-primary"
                    >
                        Add New Account
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
                                    placeholder="Search by link or description..."
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
                                    Search
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
                                        All
                                    </button>
                                    <button
                                        type="button"
                                        className={`btn ${activeFilter === true ? 'btn-primary' : 'btn-outline-primary'}`}
                                        onClick={() => handleStatusFilterChange(true)}
                                    >
                                        Active
                                    </button>
                                    <button
                                        type="button"
                                        className={`btn ${activeFilter === false ? 'btn-primary' : 'btn-outline-primary'}`}
                                        onClick={() => handleStatusFilterChange(false)}
                                    >
                                        Inactive
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
                                    <option key={size} value={size}>{size} per page</option>
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