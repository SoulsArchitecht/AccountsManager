import React, {useState} from 'react';
import {createAccount} from '../../services/AccountService';
import {useNavigate} from 'react-router-dom';
//import { v4 as uuidv4 } from 'uuid';

const AddAccount = () => {

    const [id, setId] = useState('');
    const [link, setLink] = useState('');
    const [description, setDescription] = useState('');
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [emailAnother, setEmailAnother] = useState('');
    const [nickName, setNickName] = useState('');
    const [userId, setUserId] = useState(2);

    const navigator = useNavigate();

    function saveAccount(e) {
        e.preventDefault();

        const account = {id, link, description, login, password, email, emailAnother, nickName, userId};
        createAccount(account).then((response) => {
            //setId(uuidv4());
            console.log(response.data);
            navigator('/accounts');
        })

    }

  return (
    <div className='container'>
      <br/><br/>
      <div className='row'>
        <div className='card col-md-6 offset-md-3'>
            <h2 className='text-center'>Add account</h2>
            <div className='card-body'>
                <form>

                    <div className='form-group mb-2'>
                        <label className='form-label fw-bold'>Link:</label>
                        <input
                        type='text'
                        placeholder='Enter account or resource link'
                        name='link'
                        value={link}
                        className='form-control'
                        onChange={(e) => setLink(e.target.value)}
                        >
                        </input>
                    </div>

                    <div className='form-group mb-2'>
                        <label className='form-label fw-bold'>Description:</label>
                        <input
                        type='text'
                        placeholder='Enter description'
                        name='description'
                        value={description}
                        className='form-control'
                        onChange={(e) => setDescription(e.target.value)}
                        >
                        </input>
                    </div>

                    <div className='form-group mb-2'>
                        <label className='form-label fw-bold'>Login:</label>
                        <input
                        type='text'
                        placeholder='Enter your login'
                        name='login'
                        value={login}
                        className='form-control'
                        onChange={(e) => setLogin(e.target.value)}
                        >
                        </input>
                    </div>

                    <div className='form-group mb-2'>
                        <label className='form-label fw-bold'>Password:</label>
                        <input
                        type='text'
                        placeholder='Enter your password'
                        name='password'
                        value={password}
                        className='form-control'
                        onChange={(e) => setPassword(e.target.value)}
                        >
                        </input>
                    </div>

                    <div className='form-group mb-2'>
                        <label className='form-label fw-bold'>Email:</label>
                        <input
                        type='text'
                        placeholder='Enter your email'
                        name='email'
                        value={email}
                        className='form-control'
                        onChange={(e) => setEmail(e.target.value)}
                        >
                        </input>
                    </div>

                    <div className='form-group mb-2 fw-bold'>
                        <label className='form-label'>Another Email:</label>
                        <input
                        type='text'
                        placeholder='Enter your another email if you have it'
                        name='emailAnother'
                        value={emailAnother}
                        className='form-control'
                        onChange={(e) => setEmailAnother(e.target.value)}
                        >
                        </input>
                    </div>

                    <div className='form-group mb-2'>
                        <label className='form-label fw-bold'>Nickname:</label>
                        <input
                        type='text'
                        placeholder='Enter your nickname if resource need it'
                        name='nickName'
                        value={nickName}
                        className='form-control'
                        onChange={(e) => setNickName(e.target.value)}
                        >
                        </input>
                    </div>
                    

                    <button className='btn btn-success' onClick={saveAccount}>Submit</button>
                    <button className='btn btn-danger pr-2'>Clear</button>
                </form>
            </div>
        </div>
      </div>
    </div>
  )
}

export default AddAccount;
