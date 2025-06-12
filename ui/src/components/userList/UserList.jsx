import React, {useState, useEffect, useMemo} from 'react';
import {getAllUsers} from '../../services/UserService';
import {useNavigate} from 'react-router-dom'
import { deleteUser, findByKeyword } from '../../services/UserService';
import {format} from 'date-fns';
import { useTable } from 'react-table';
import Pagination from '@material-ui/lab/Pagination';
import { useAuth } from '../../authContext/AuthContext';
import Unauthorized from '../../common/Unauthorized';


const UserList = () => {
    const [data, setData] = useState({
        users: [],
        totalPages: 0,
        totalElements: 0,
        loading: true,
        error: null
    });
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(5);
    const [keyword, setKeyword] = useState('');
    const { user, token } = useAuth();
    const navigate = useNavigate();

    // Проверка на роль ADMIN
    const isAdmin = user?.role === 'ROLE_ADMIN';

    const fetchUsers = async () => {
        if (!isAdmin) return;

        try {
            setData(prev => ({ ...prev, loading: true, error: null }));
            const params = {
                page,
                size: pageSize,
                ...(keyword && { keyword })
            };
            
            const response = await getAllUsers(params);
            setData({
                users: response.data.content || [],
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                loading: false,
                error: null
            });
        } catch (error) {
            setData({
                users: [],
                totalPages: 0,
                totalElements: 0,
                loading: false,
                error: error.response?.data?.message || 'Failed to fetch users'
            });
            console.error('Error fetching users:', error);
        }
    };

    useEffect(() => {
        if (isAdmin) {
            fetchUsers();
        }
    }, [page, pageSize, keyword, token, isAdmin]);

    const columns = useMemo(() => [
        { Header: 'Email', accessor: 'email' },
        { Header: 'Login', accessor: 'login' },
        { 
            Header: 'Role', 
            accessor: 'role',
            Cell: ({ value }) => value?.replace('ROLE_', '')
        },
        { 
            Header: 'Status', 
            accessor: 'status',
            Cell: ({ value }) => value ? 'Active' : 'Inactive'
        },
        {
            Header: 'Actions',
            accessor: 'actions',
            Cell: ({ row }) => (
                <div>
                    <button 
                        onClick={() => navigate(`/edit-user/${row.original.id}`)}
                        className="btn btn-warning btn-sm mr-2"
                    >
                        Edit
                    </button>
                    <button 
                        onClick={() => console.log('Delete', row.original.id)}
                        className="btn btn-danger btn-sm"
                    >
                        Delete
                    </button>
                </div>
            )
        }
    ], []);

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
                    <div className="row mb-3">
                        <div className="col-md-6">
                            <div className="input-group">
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Search by email or login..."
                                    value={keyword}
                                    onChange={(e) => setKeyword(e.target.value)}
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