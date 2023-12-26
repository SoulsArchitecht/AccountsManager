import React, {useState, useEffect} from 'react';
import {accountList} from '../../services/AccountService';

const AccountList = () => {

    const [accounts, setAccounts] = useState([]);

    useEffect(() => {
        accountList().then((response) => {
            setAccounts(response.data);
        }).catch(error => {
            console.error(error);
        })
    }, [])

  return (
    <div className='container'>
        <h2 className="text-center">Accounts List</h2>
            <br></br>
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
                        <th>actions</th>
                    </tr>    
                </thead>

                <tbody>
                    {
                        accounts.map(
                            account =>
                            <tr key={account.id}>
                                <td>{account.link}</td>
                                <td>{account.description}</td>
                                <td>{account.createdAt}</td>
                                <td>{account.changedAt}</td>
                                <td>{account.login}</td>
                                <td>{account.password}</td>
                                <td>{account.email}</td>
                                <td>{account.emailAnother}</td>
                                <td>{account.nickname}</td>
                                <td>{account.active}</td>
                                <td></td>

                            </tr>
                        )
                    }
                </tbody>    
            </table>                     
    </div>
  )
}

export default AccountList;
