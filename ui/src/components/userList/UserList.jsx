import React, {useState, useEffect, useMemo, useCallback} from 'react';
import {getAllUsers, deleteUser, toggleStatus, findByKeyword} from '../../services/UserService';
import {useNavigate} from 'react-router-dom'
import {format} from 'date-fns';
import { useTable } from 'react-table';
import Pagination from '@mui/material/Pagination';
import { useAuth } from '../../authContext/AuthContext';
import Unauthorized from '../../common/Unauthorized';
import '../userList/UserList.css'
import { useLocalization } from '../../context/LocalizationContext';


const UserList = () => {
    const [data, setData] = useState({
        users: [],
        totalPages: 0,
        totalElements: 0,
        loading: true,
        error: null,
        success: null
    });
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(5);
    const [keyword, setKeyword] = useState('');
    const [statusFilter, setStatusFilter] = useState(null);
    const [sortField, setSortField] = useState('email');
    const [sortDirection, setSortDirection] = useState('desc');
    const { user, token } = useAuth();
    const navigate = useNavigate();
    const { t } = useLocalization();

    const isAdmin = user?.role === 'ROLE_ADMIN';

    const fetchUsers = useCallback(async () => {
        if (!isAdmin) return;

        try {
            setData(prev => ({ ...prev, loading: true, error: null, success: null }));
            const params = {
                page,
                size: pageSize,
                keyword: keyword || undefined,
                status: statusFilter,
                sort: `${sortField},${sortDirection}`
            };

            Object.keys(params).forEach(key => params[key] === undefined && delete params[key]);
            
            const response = await getAllUsers(params);
            setData({
                users: response.data.content || [],
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                loading: false,
                error: null,
                success: null
            });
        } catch (error) {
            setData({
                users: [],
                totalPages: 0,
                totalElements: 0,
                loading: false,
                error: error.response?.data?.message || t('table.user.load.error'),
                success: null
            });
            console.error('Error fetching users:', error);
        }
    }, [page, pageSize, keyword, statusFilter, sortField, sortDirection, token]);

    useEffect(() => {
        if (isAdmin) {
            const timer = setTimeout(() => {
                fetchUsers();
            }, 500);

            return () => clearTimeout(timer);
        }
    }, [fetchUsers]);

    const handleStatusFilterChange = (value) => {
        setStatusFilter(value);
        setPage(0);
    };

    const removeUser = async (id) => {
        if (window.confirm(t('table.user.delete.confirm'))) {
            deleteUser(id)
                .then(() => {
                    setData(prev => ({
                        ...prev,
                        error: null,
                        success: t('table.user.delete.success')
                    }));
                    fetchUsers();
                })
                .catch(error => {
                    console.error('Delete error: ', error);
                    setData(prev => ({
                        ...prev,
                        error: error.response?.data?.message || t('table.user.delete.error'),
                        success: null
                    }));
                });
        };
    };

    const toggleUserStatus = async (id, currentStatus) => {
        const action = currentStatus ? 'deactivate' : 'activate';
        if (!window.confirm(`Are you sure you want to ${action} this user`)) {
            return;
        }

        try {
            await toggleStatus(id);
            setData(prev => ({
                ...prev,
                error: null,
                success: `User ${action}d successfully`
            }));
            fetchUsers();
        } catch (error) {
            console.error('Status change error: ', error);
            setData(prev => ({
                ...prev,
                error: error.response?.data?.message || `Failed to ${action} user`,
                success: null
            }));
        };
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
            Header: () => (
                <span 
                    className="sortable-header"
                    onClick={() => handleSort('email')}
                >
                    {t('table.user.email')} {sortField === 'email' && (sortDirection === 'asc' ? '↑' : '↓')}
                </span>
            ), 
            accessor: 'email' 
        },
        { 
            Header: () => (
                <span 
                    className="sortable-header"
                    onClick={() => handleSort('login')}
                >
                    {t('table.user.nickname')} {sortField === 'login' && (sortDirection === 'asc' ? '↑' : '↓')}
                </span>
            ), 
            accessor: 'login' 
        },
        { 
            Header: t('table.user.role'), 
            accessor: 'role',
            Cell: ({ value }) => value?.replace('ROLE_', '')
        },
        { 
            Header: t('table.user.status'), 
            accessor: 'status',
            Cell: ({ value }) => (
                <span className={`badge ${value ? 'bg-success' : 'bg-secondary'}`}>
                    {value ? 'Active' : 'Inactive'}
                </span>
            )
        },
        {
            Header: t('table.user.actions'),
            accessor: 'actions',
            Cell: ({ row }) => (
                <div className="actions-container">
                    <button 
                        onClick={() => navigate(`/edit-user/${row.original.id}`)}
                        className="action-btn btn-edit"
                    >
                        {t('table.user.button.edit')}
                    </button>
                    <button
                        onClick= { () => toggleUserStatus(row.original.id, row.original.status)}
                        className={`action-btn ${row.original.status ? 'btn-deactivate' : 'btn-activate'}`}
                    >
                        {row.original.status ? t('table.user.button.deactivate') : t('table.user.button.activate')}    
                    </button>
                    <button 
                        onClick={() => removeUser(row.original.id)}
                        className="action-btn btn-delete"
                    >
                        {t('table.user.button.delete')}
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
    } = useTable({ columns, data: data.users });

    if (!isAdmin) {
        return <Unauthorized 
            title="Access Denied" 
            message="You must be an administrator to view this page" 
        />;
    }

    if (data.loading) return <div className="text-center mt-4">{t('commmon.loading')}</div>;
    if (data.error) return <div className="alert alert-danger">{data.error}</div>;

    return (
        <div className="container mt-4">
            <div className="card">
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h3>{t('table.user.title')}</h3>
                    <button 
                        onClick={() => navigate('/add-user')}
                        className="btn btn-primary"
                    >
                        {t('table.user.button.add')}
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
                                    placeholder={t('table.user.search.help')}
                                    value={keyword}
                                    onChange={(e) => setKeyword(e.target.value)}
                                    onKeyDown={(e) => e.key === 'Enter' && fetchUsers()}
                                />
                                <button 
                                    className="btn btn-outline-secondary"
                                    onClick={() => {
                                        setPage(0);
                                        fetchUsers();
                                    }}
                                >
                                    {t('table.user.button.search')}
                                </button>
                            </div>
                        </div>
                        <div className="col-md-6">
                            <div className="d-flex align-items-center justify-content-end">
                                <div className="btn-group" role="group">
                                    <button
                                        type="button"
                                        className={`btn ${statusFilter === null ? 'btn-primary' : 'btn-outline-primary'}`}
                                        onClick={() => handleStatusFilterChange(null)}
                                    >
                                        {t('toggle.all')}
                                    </button>
                                    <button
                                        type="button"
                                        className={`btn ${statusFilter === true ? 'btn-primary' : 'btn-outline-primary'}`}
                                        onClick={() => handleStatusFilterChange(true)}
                                    >
                                        {t('toggle.active')}
                                    </button>
                                    <button
                                        type="button"
                                        className={`btn ${statusFilter === false ? 'btn-primary' : 'btn-outline-primary'}`}
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

export default UserList;