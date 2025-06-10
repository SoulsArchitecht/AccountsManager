import { useState } from 'react';
import { useAuth } from '../authContext/AuthContext';
import { useNavigate } from 'react-router-dom';

const LoginForm = () => {
    const [email, setEmail] = useState('');
    const[password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            await login(email, password);
        } catch (err) {
            setError(err.message || 'Failed to login');
        }

    };

    return (
        <div className = "auth-form">
            <h2>Login</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit = {handleSubmit}>
                <div className = "form-group">
                    <label>Email:</label>
                    <input
                        type="email"
                        className="form-control"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />    
                </div>
                <div className = "form-group">
                    <label>Password:</label>
                    <input
                        type="password"
                        className="form-control"
                        value={email}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        minLength="8"
                    />    
                </div>
                <button type="submit" className="btn btn-primary">Login</button>
            </form>
            <p>
                Don't have an account?{' '}
                <button className="btn btn-link" onClick={() => navigate('/register')}>
                    Register
                </button>    
            </p>
        </div>
    );
};

export default LoginForm;