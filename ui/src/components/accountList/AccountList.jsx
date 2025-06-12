import React, { useState, useEffect, useMemo } from 'react';
import { getAllAccounts } from '../../services/AccountService';
import { useNavigate } from 'react-router-dom';
import { deleteAccount, findByKeyword } from '../../services/AccountService';
import { format } from 'date-fns';
import { useTable } from 'react-table';
import Pagination from '@material-ui/lab/Pagination';
import { useAuth } from '../../authContext/AuthContext';

const AccountList = () => {
    const [data, setData] = useState({
        accounts: [],
        totalPages: 0,
        totalElements: 0,
        loading: true,
        error: null
    });
    const [page, setPage] = useState(0); // Spring использует 0-based индексацию
    const [pageSize, setPageSize] = useState(5);
    const [keyword, setKeyword] = useState('');
    const { token } = useAuth();
    const navigate = useNavigate();

    const fetchAccounts = async () => {
        try {
            setData(prev => ({ ...prev, loading: true, error: null }));
            const params = {
                page,
                size: pageSize,
                ...(keyword && { keyword })
            };
            
            const response = await getAllAccounts(params);
            setData({
                accounts: response.data.content || [],
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                loading: false,
                error: null
            });
        } catch (error) {
            setData({
                accounts: [],
                totalPages: 0,
                totalElements: 0,
                loading: false,
                error: error.response?.data?.message || 'Failed to fetch accounts'
            });
            console.error('Error fetching accounts:', error);
        }
    };

    useEffect(() => {
        fetchAccounts();
    }, [page, pageSize, keyword, token]);

    const columns = useMemo(() => [
        { Header: 'Link', accessor: 'link' },
        { Header: 'Description', accessor: 'description' },
        { 
            Header: 'Created At', 
            accessor: 'createdAt',
            Cell: ({ value }) => format(new Date(value), 'dd.MM.yyyy HH:mm')
        },
        { 
            Header: 'Updated At', 
            accessor: 'changedAt',
            Cell: ({ value }) => format(new Date(value), 'dd.MM.yyyy HH:mm')
        },
        { Header: 'Login', accessor: 'login' },
        { Header: 'Email', accessor: 'email' },
        { 
            Header: 'Active', 
            accessor: 'active',
            Cell: ({ value }) => value ? 'Yes' : 'No'
        },
        {
            Header: 'Actions',
            accessor: 'actions',
            Cell: ({ row }) => (
                <div>
                    <button 
                        onClick={() => navigate(`/edit-account/${row.original.id}`)}
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
    } = useTable({ columns, data: data.accounts });

    if (data.loading) return <div className="text-center mt-4">Loading...</div>;
    if (data.error) return <div className="alert alert-danger">{data.error}</div>;

    return (
        <div className="container mt-4">
            <div className="card">
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h3>Accounts</h3>
                    <button 
                        onClick={() => navigate('/add-account')}
                        className="btn btn-primary"
                    >
                        Add New Account
                    </button>
                </div>
                
                <div className="card-body">
                    <div className="row mb-3">
                        <div className="col-md-6">
                            <div className="input-group">
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Search..."
                                    value={keyword}
                                    onChange={(e) => setKeyword(e.target.value)}
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
                            page={page + 1} // Material-UI использует 1-based индексацию
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