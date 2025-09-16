import {findByKeyword} from '../../services/AccountService';
import {useState, useEffect} from 'react';
//import {getAllAccounts} from '../../components/accountList';
import HomePage from '../homePage/HomePage';


const SearchPanel = () => {

    const [keyword, setKeyword] = useState("");
    const [accounts, setAccounts] = useState([]);

    const onChangeSearchKeyword = e => {
        const keyword = e.target.value;
        setKeyword(keyword);
      };

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

    useEffect(() => {
        findByKeyword();
    }, [])

    function Result () {
      if (keyword) {
        return (
          <div className='container'>
          <h2 className="text-center mt-2">Accounts List</h2>
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
                                  <td>{account.createdAt}</td>
                                  <td>{account.changedAt}</td>
                                  <td>{account.login}</td>
                                  <td>{account.password}</td>
                                  <td>{account.email}</td>
                                  <td>{account.emailAnother}</td>
                                  <td>{account.nickName}</td>
                                  <td>{account.active}</td>
                                  {/* <td>
                                      <button className='btn btn-info' onClick={() => updateAccount(account.id)}>Update</button>
                                      <button className='btn btn-danger' onClick={() => removeAccount(account.id)}>Delete</button>
                                  </td> */}
  
                              </tr>
                          )
                      }
                  </tbody>    
              </table>                     
      </div>      
        )
      } else {
        return <HomePage/>
      }

    } 


    return (
  
    <div className="col-md-8">
    <br></br> 
        <div className="input-group mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="Search by keyword"
            value={keyword}
            onChange={onChangeSearchKeyword}
          />
          <div className="input-group-append">
            <button
              className="btn btn-outline-secondary"
              type="button"
              onClick={getByKeyword}
              onPress={getByKeyword}
            >
              Search
            </button>
          </div>
        </div>

        <Result/>
    </div>
    

    )
}

export default SearchPanel;
