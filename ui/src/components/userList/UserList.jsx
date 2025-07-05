import React, {useState, useEffect, useMemo, useCallback} from 'react';
import {getAllUsers, deleteUser, toggleStatus, findByKeyword} from '../../services/UserService';
import {useNavigate} from 'react-router-dom'
import {format} from 'date-fns';
import { useTable } from 'react-table';
import Pagination from '@mui/material/Pagination';
import { useAuth } from '../../authContext/AuthContext';
import Unauthorized from '../../common/Unauthorized';
import '../userList/UserList.css'


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

    // Проверка на роль ADMIN
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
                error: error.response?.data?.message || 'Failed to fetch users',
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
        if (window.confirm(`Are you sure to delete this user?`)) {
            deleteUser(id)
                .then(() => {
                    setData(prev => ({
                        ...prev,
                        error: null,
                        success: 'User deleted successfully'
                    }));
                    fetchUsers();
                })
                .catch(error => {
                    console.error('Delete error: ', error);
                    setData(prev => ({
                        ...prev,
                        error: error.response?.data?.message || 'Failed to delete user',
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
                    Email {sortField === 'email' && (sortDirection === 'asc' ? '↑' : '↓')}
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
                    Login {sortField === 'login' && (sortDirection === 'asc' ? '↑' : '↓')}
                </span>
            ), 
            accessor: 'login' 
        },
        { 
            Header: 'Role', 
            accessor: 'role',
            Cell: ({ value }) => value?.replace('ROLE_', '')
        },
        { 
            Header: 'Status', 
            accessor: 'status',
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
                <div className="actions-container">
                    <button 
                        onClick={() => navigate(`/edit-user/${row.original.id}`)}
                        className="action-btn btn-edit"
                    >
                        Edit
                    </button>
                    <button
                        onClick= { () => toggleUserStatus(row.original.id, row.original.status)}
                        className={`action-btn ${row.original.status ? 'btn-deactivate' : 'btn-activate'}`}
                    >
                        {row.original.status ? 'Deactivate' : 'Activate'}    
                    </button>
                    <button 
                        onClick={() => removeUser(row.original.id)}
                        className="action-btn btn-delete"
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
    } = useTable({ columns, data: data.users });

    if (!isAdmin) {
        return <Unauthorized 
            title="Access Denied" 
            message="You must be an administrator to view this page" 
        />;
    }

    if (data.loading) return <div className="text-center mt-4">Loading users...</div>;
    if (data.error) return <div className="alert alert-danger">{data.error}</div>;

    return (
        <div className="container mt-4">
            <div className="card">
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h3>User Management</h3>
                    <button 
                        onClick={() => navigate('/add-user')}
                        className="btn btn-primary"
                    >
                        Add New User
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
                                    placeholder="Search by email or login..."
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
                                    Search
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
                                        All
                                    </button>
                                    <button
                                        type="button"
                                        className={`btn ${statusFilter === true ? 'btn-primary' : 'btn-outline-primary'}`}
                                        onClick={() => handleStatusFilterChange(true)}
                                    >
                                        Active
                                    </button>
                                    <button
                                        type="button"
                                        className={`btn ${statusFilter === false ? 'btn-primary' : 'btn-outline-primary'}`}
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

export default UserList;