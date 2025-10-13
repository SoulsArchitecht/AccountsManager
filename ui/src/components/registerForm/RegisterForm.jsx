import { useState } from 'react';
import { useAuth } from '../../authContext/AuthContext';
import { useNavigate } from 'react-router-dom';
import { useLocalization } from '../../context/LocalizationContext';

const RegisterForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [login, setLogin] = useState('');
    const [error, setError] = useState('');
    const { register } = useAuth();
    const navigate = useNavigate();
    const { t } = useLocalization();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const result = await register(email, password, login);
        if (result.success) {
            navigate('/');
        } else {
            setError(result.error || 'Login failed');
        };
    };

    return (
        <div className="container mt-5">
          <div className="row justify-content-center">
            <div className="col-md-6">
              <div className="card">
                <div className="card-body">
                  <h2 className="card-title text-center">{t('auth.register')}</h2>
                  {error && <div className="alert alert-danger">{error}</div>}
                  <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                      <label htmlFor="email" className="form-label">{t('auth.email')}</label>
                      <input
                        type="email"
                        className="form-control"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                      />
                    </div>
                    <div className="mb-3">
                      <label htmlFor="password" className="form-label">{t('auth.password')}({t('help.password')}):</label>
                      <input
                        type="password"
                        className="form-control"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        minLength="8"
                      />
                    </div>
                    <div className="mb-3">
                      <label htmlFor="login" className="form-label">{t('auth.nickname')}</label>
                      <input
                        type="text"
                        className="form-control"
                        id="login"
                        value={login}
                        onChange={(e) => setLogin(e.target.value)}
                        required
                        minLength="1"
                        maxLength="255"
                      />
                    </div>
                    <div className="d-grid">
                      <button type="submit" className="btn btn-primary">{t('auth.sign_up')}</button>
                    </div>
                  </form>
                  <div className="mt-3 text-center">
                    {t('help.already_registered')}{' '}
                    <button className="btn btn-link p-0" onClick={() => navigate('/login')}>
                      {t('auth.sign_in')}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      );    
};

export default RegisterForm;