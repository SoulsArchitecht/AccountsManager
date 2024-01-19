import React, {useState} from 'react';
import {createAccount} from '../../services/AccountService';
import {useNavigate} from 'react-router-dom';
//import { v4 as uuidv4 } from 'uuid';
import * as Const from '../../common/Const';

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

    const [errors, setErrors] = useState({
        link: '',
        login: '',
        password: '',
        email: '',
        emailAnother
    })

    const navigator = useNavigate();

    function isEmpty(str) {
        return (!str || str.length === 0 );
    }

    function saveAccount(e) {
        e.preventDefault();

        if (validateForm()) {
            const account = {id, link, description, login, password, email, emailAnother, nickName, userId};
            createAccount(account).then((response) => {
                //setId(uuidv4());
                console.log(response.data);
                navigator('/accounts');
            })
        }
    }

    function validateForm() {
        let valid = true;

        const errorsCopy = {...errors}

        if (link.match(Const.REGEX_LINK)) {
            errorsCopy.link = '';
        } else if (isEmpty(link) || !link.match(Const.REGEX_LINK)) {
            errorsCopy.link = 'Link is invalid or blank';
            valid = false;
        }

        if (login.length >= 3) {
            errorsCopy.login = '';
        } else {
            errorsCopy.login = 'Login is required';
            valid = false;
        }

        if (password.length >= 8) {
            errorsCopy.password = '';
        } else {
            errorsCopy.password = 'Password length is shoter than 8 or password is blank';
            valid = false;
        }

        if (email.match(Const.REGEX_EMAIL)) {
            errorsCopy.email = '';
        } else {
            errorsCopy.email = 'Email is invalid or blank';
            valid = false;
        }

        if (!emailAnother) {
            errorsCopy.emailAnother = '';
        } else if (emailAnother.match(Const.REGEX_EMAIL)){
            errorsCopy.emailAnother = '';
        } else {
            errorsCopy.emailAnother ='Email is invalid';
            valid = false;
        }

        setErrors(errorsCopy);

        return valid;
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
                        className={`form-control ${errors.link ? 'is-invalid' : ''}`}
                        onChange={(e) => setLink(e.target.value)}
                        >
                        </input>
                        {errors.link && <div className='invalid-feedback'>{errors.link}</div>}
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
                        className={`form-control ${errors.login ? 'is-invalid' : ''}`}
                        onChange={(e) => setLogin(e.target.value)}
                        >
                        </input>
                        {errors.login && <div className='invalid-feedback'>{errors.login}</div>}
                    </div>

                    <div className='form-group mb-2'>
                        <label className='form-label fw-bold'>Password:</label>
                        <input
                        type='text'
                        placeholder='Enter your password'
                        name='password'
                        value={password}
                        className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                        onChange={(e) => setPassword(e.target.value)}
                        >
                        </input>
                        {errors.password && <div className='invalid-feedback'>{errors.password}</div>}
                    </div>

                    <div className='form-group mb-2'>
                        <label className='form-label fw-bold'>Email:</label>
                        <input
                        type='text'
                        placeholder='Enter your email'
                        name='email'
                        value={email}
                        className={`form-control ${errors.email ? 'is-invalid' : ''}`}
                        onChange={(e) => setEmail(e.target.value)}
                        >
                        </input>
                        {errors.email && <div className='invalid-feedback'>{errors.email}</div>}
                    </div>

                    <div className='form-group mb-2 fw-bold'>
                        <label className='form-label'>Another Email:</label>
                        <input
                        type='text'
                        placeholder='Enter your another email if you have it'
                        name='emailAnother'
                        value={emailAnother}
                        className={`form-control ${errors.emailAnother ? 'is-invalid' : ''}`}
                        onChange={(e) => setEmailAnother(e.target.value)}
                        >
                        </input>
                        {errors.emailAnother && <div className='invalid-feedback'>{errors.emailAnother}</div>}
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
