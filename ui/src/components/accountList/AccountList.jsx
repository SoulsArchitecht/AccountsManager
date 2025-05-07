import React, {useState, useEffect, useRef, useMemo} from 'react';
import {getAllAccounts} from '../../services/AccountService';
import {useNavigate} from 'react-router-dom'
import { deleteAccount, findByKeyword } from '../../services/AccountService';
import {format} from 'date-fns';
import { useTable } from 'react-table';
import Pagination from '@material-ui/lab/Pagination';
//import history from 'history';
//import { Typography } from '@material-ui/core';


const AccountList = (props) => {

    const [accounts, setAccounts] = useState([]);
    const [keyword, setKeyword] = useState("");
    const [page, setPage] = useState(1);
    const [count, setCount] = useState(0);
    const [pageSize, setPageSize] = useState(5);
    const accountsRef = useRef();

    const pageSizes = [10, 20, 40];
    accountsRef.current = accounts;
    

    const navigator = useNavigate();

    const onChangeSearchKeyword = (e) => {
        const keyword = e.target.value;
        setKeyword(keyword);
    }

    const getByKeyword = () => {
        findByKeyword(keyword)
          .then(response => {
            setAccounts(response.data);
            console.log(response.data);
          })
          .catch(e => {
            console.log(e);
          });
      };

    const getRequestParams = (keyword, page, pageSize) => {
        let params = {};

        if (keyword) {
            params["keyword"] = keyword;
        }

        if (page) {
            params["page"] = page - 1;
        }

        if (pageSize) {
            params["size"] = pageSize;
        }

        return params;
    }

    // useEffect(() => {
    //     getAllAccounts();
    // }, [])

    // function getAllAccounts() {
    //     accountList().then((response) => {
    //         setAccounts(response.data);
    //     }).catch(error => {
    //         console.error(error);
    //     })
    // }

    const retrieveAccounts = () => {
        const params = getRequestParams(keyword, page, pageSize);

        getAllAccounts(params)
            .then((response) => {
                //const {accounts, totalPages} = response.data;
                const accounts = response.data.data;
                const totalPages = response.data.total;

                setAccounts(accounts);
                setCount(totalPages);
                console.log(response.data);
            })
            .catch((e) => {
                console.log(e);
            })
    }

    function addNewAccount() {
        navigator('/add-account');
    }

    function updateAccount(id) {
        navigator(`/edit-account/${id}`);
    }

    // function formattedDate(date) {
    //     return new Intl.DateTimeFormat('en-US', { year: 'numeric', month: 'long', day: '2-digit' }).format(date);
    // }    

    function formatDate(date) {
        return format(date, 'dd.MM.yyyy');
    }
    // function removeAccount(id) {
    //     deleteAccount(id).then((response) => {
    //         getAllAccounts(response.data);
    //     }).catch(error => {
    //         console.error(error);
    //     })
    // }

    //  function formatDate(date) {
    //      return moment(date).format("DD MMM YYYY");
    //  }

    // function formatDate(date) {
    //     return new Date(date).toLocaleDateString("en-US");
    // }

    useEffect(retrieveAccounts, [keyword, page, pageSize]);

    const findByKeyword = () => {
        setPage(1);
        retrieveAccounts();
    }

    const removeAccount = (rowIndex) => {
        const id = accountsRef.current[rowIndex].id;

        deleteAccount(id)
            .then((response) => {
                //history.push("/accounts");

                //const newAccounts = [...accounts];
                //newAccounts.splice(rowIndex, 1);

                //setAccounts(newAccounts);
                retrieveAccounts();
            })
            .catch((e) => {
                console.log(e);
            })

    }

    const handlePageChange = (event, value) => {
        setPage(value);
    }

    const handlePageSizeChange = (event) => {
        setPageSize(event.target.value);
        setPage(1);
    }

    const columns = useMemo(
        () => [
            {
                Header: "Link",
                accessor: "link",
            },
            {
                Header: "Description",
                accessor: "description",
            },
            {
                Header: "Created At",
                accessor: "createdAt",
                Cell: ({row}) => {
                    //return <span>{row.original.createdAt}</span>;
                    return <span>{formatDate(row.original.createdAt)}</span>
                }
            },
            {
                Header: "Changed At",
                accessor: "changedAt",
                Cell: ({ row }) => {
                    return <span>{formatDate(row.original.changedAt)}</span>;
                }
            },
            {
                Header: "Login",
                accessor: "login",
            },
            {
                Header: "Password",
                accessor: "password",
            },
            {
                Header: "Email",
                accessor: "email",
            },
            {
                Header: "Email Another",
                accessor: "emailAnother",
            },
            {
                Header: "NickName",
                accessor: "nickName",
            },
            {
                Header: "Active",
                accessor: "active",
                Cell: ({ row }) => {
                    if (row.original.active === true) {
                        return "YES";
                    } else {
                        return "NO";
                    }
                }
            },
            {
                Header: "Actions",
                accessor: "actions",
                Cell: (props) => {
                    const rowIdx = props.row.id;
                    return (
                        <div>
                            <span onClick = {() => updateAccount(rowIdx)}>
                                <button type="button"
                                className = "btn btn-warning btn-sm">Edit</button>
                            </span>
                            &nbsp;
                            <span onClick = {() => removeAccount(rowIdx)}>
                                <button type="button"
                                className = "btn btn-danger btn-sm">Delete</button>
                            </span>
                        </div>
                    );
                },
            },

        ],
        []
    );

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
    } = useTable({
        columns,
        data: accounts,
    });

  return (
    <div className = "list row">
        <br></br>

        <div className = "col-md-8">
            <div className = "input-group mb-3">
                <input
                type="text"
                className="form-control"
                placeholder="keyword"
                value={keyword}
                onChange={onChangeSearchKeyword}
                />
                <div className="input-group-append">
                    <button
                        className="btn btn-outline-success"
                        type="button"
                        onClick={findByKeyword}
                    >
                        Search
                    </button>
                </div>
            </div>
        </div>

        <div className = "col-md-12 list">
            <div className = "mt-3">
                {"Items per Page: "}
                <select onChange={handlePageSizeChange} value = {pageSize}>
                    {pageSizes.map((size) => (
                        <option key = {size} value = {size}>
                            {size}
                        </option>
                    ))}
                </select>

                <Pagination
                    color = "primary"
                    className = "my-3"
                    count = {count}
                    page = {page}
                    siblingCount = {1}
                    boundaryCount = {1}
                    variant = "outlined"
                    onChange = {handlePageChange}
                />
            </div>

            <table
                className = "table table-striped table-bordered"
                {...getTableProps()}
            >

                <thead>
                    {headerGroups.map((headerGroup) => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                            {headerGroup.headers.map((column) => (
                                <th {...column.getHeaderProps()}>
                                    {column.render("Header")}
                                </th>
                            ))}
                        </tr>
                    ))}
                </thead>

                <tbody {...getTableBodyProps()}>
                    {rows.map((row, i) => {
                        prepareRow(row);
                        return (
                            <tr {...row.getRowProps()}>
                                {row.cells.map((cell) => {
                                    return (
                                        <td {...cell.getCellProps()}>
                                            {cell.render("Cell")}
                                        </td>
                                    );
                                })}
                            </tr>
                        )
                    })}
                </tbody>

            </table>

            <div className="mt-3">
                {"Items per Page: "}
                <select onChange={handlePageSizeChange} value={pageSize}>
                    {pageSizes.map((size) => (
                    <option key={size} value={size}>
                        {size}
                    </option>
                    ))}
                </select>

                <Pagination
                    color="primary"
                    className="my-3"
                    count={count}
                    page={page}
                    siblingCount={1}
                    boundaryCount={1}
                    variant="outlined"
                    onChange={handlePageChange}
                />
            </div>    


        </div>
    </div>
  )
}

export default AccountList;
