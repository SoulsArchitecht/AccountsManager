import React, {useState, useEffect} from 'react';
import {accountList} from '../../services/AccountService';
import {useNavigate} from 'react-router-dom'
import { deleteAccount } from '../../services/AccountService';
import moment from 'moment';


const AccountList = () => {

    const [accounts, setAccounts] = useState([]);

    const navigator = useNavigate();

    useEffect(() => {
        getAllAccounts();
    }, [])

    function getAllAccounts() {
        accountList().then((response) => {
            setAccounts(response.data);
        }).catch(error => {
            console.error(error);
        })
    }

    function addNewAccount() {
        navigator('/add-account');
    }

    function updateAccount(id) {
        navigator(`/edit-account/${id}`);
    }

    function removeAccount(id) {
        deleteAccount(id).then((response) => {
            getAllAccounts(response.data);
        }).catch(error => {
            console.error(error);
        })
    }

    function formatDate(date) {
        return moment(date).format("DD MMM YYYY");
    }

  return (
    <div className='container'>
        <h2 className="text-center mt-2">Accounts List</h2>
            <br></br>
            <button className='btn btn-primary mb-2' onClick={addNewAccount}>Add</button>
            <table className="table table-dark table-striped table-bordered">
                <thead>
                    <tr>
                        <th>link</th>
                        <th>description</th>
                        <th>created at</th>
                        <th>changed at</th>
                        <th>login</th>
                        <th>password</th>
                        <th>email</th>
                        <th>emailAnother</th>
                        <th>nickname</th>
                        <th>active</th>
                        <th>Actions</th>
                    </tr>    
                </thead>

                <tbody>
                    {
                        accounts.map(
                            account =>
                            <tr key={account.id}>
                                <td>{account.link}</td>
                                <td>{account.description}</td>
                                <td>{formatDate(account.createdAt)}</td>
                                <td>{formatDate(account.changedAt)}</td>
                                <td>{account.login}</td>
                                <td>{account.password}</td>
                                <td>{account.email}</td>
                                <td>{account.emailAnother}</td>
                                <td>{account.nickName}</td>
                                <td>{account.active}</td>
                                <td>
                                    <button className='btn btn-info' onClick={() => updateAccount(account.id)}>Update</button>
                                    <button className='btn btn-danger' onClick={() => removeAccount(account.id)}>Delete</button>
                                </td>

                            </tr>
                        )
                    }
                </tbody>    
            </table>                     
    </div>
  )
}

export default AccountList;
