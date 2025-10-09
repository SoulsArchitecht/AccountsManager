import { Link } from 'react-router-dom';
import { useAuth } from '../../authContext/AuthContext';
import { FaListUl, FaHome, FaPlus, FaSearch, FaUsers, FaCog, FaSignOutAlt, FaSignInAlt, FaKey } from 'react-icons/fa';
import '../header/Header.css';
import { useLocalization } from './contexts/LocalizationContext';

const Header = () => {
  const { token, logout, user, userInfo } = useAuth();
  const { t, loading } = useLocalization();

  return (
    <nav className="navbar navbar-expand navbar-dark bg-dark px-4">
      <div className="container-fluid">
        <Link to="/accounts" className="navbar-brand d-flex align-items-center">
          <FaKey className="me-2" />
          <span className="d-none d-sm-inline">{t('app.nav.title')}</span>
        </Link>

        <div className="navbar-nav me-auto">
          <li className="nav-item">
            <Link to="/accounts" className="nav-link d-flex align-items-center">
              <FaListUl className="me-1" />
              <span className="ms-1">{t('app.nav.list')}</span>
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/add-account" className="nav-link d-flex align-items-center">
              <FaPlus className="me-1" />
              <span className="ms-1">{t('app.nav.add')}</span>
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/accounts/search" className="nav-link d-flex align-items-center">
              <FaSearch className="me-1" />
              <span className="ms-1">{t('app.nav.search')}</span>
            </Link>
          </li>
          <li className="nav-item">
                <Link to={"/"} className="nav-link">
                  <FaHome className="me-1" />
                  <span className="ms-1">{t('app.nav.home')}</span>
                </Link>
              </li>
          {user?.role?.includes('ROLE_ADMIN') && (
            <li className="nav-item">
              <Link to="/users" className="nav-link d-flex align-items-center">
                <FaUsers className="me-1" />
                <span className="ms-1">Users</span>
              </Link>
            </li>
          )}

        </div>

        {/* Правые элементы */}
        <div className="navbar-nav ms-auto">
          {token ? (
            <>
              <li className="nav-item">
                <Link to="/settings" className="nav-link d-flex align-items-center">
                  {/* <FaCog className="me-1" /> */}
                  {user?.userInfo?.avatarUrl ? (
                    <img 
                      //src={`http://localhost:8080/uploads/${user?.userInfo.avatarUrl}`}
                      src={`/uploads/${user.userInfo.avatarUrl}`} 
                      alt="User Avatar"
                      className="rounded-circle me-2"
                      width="24"
                      height="24"
                      style={{ objectFit: 'cover' }}
                    />
                  ) : (
                    <div className="avatar-placeholder me-2">
                      {user?.email?.charAt(0).toUpperCase()}
                    </div>
                  )}
                  <span className="ms-1">{user?.email}</span>
                </Link>
              </li>
              <li className="nav-item">
                <button 
                  className="nav-link btn btn-link d-flex align-items-center"
                  onClick={logout}
                >
                  <FaSignOutAlt className="me-1" />
                  <span className="ms-1">{t('app.nav.logout')}</span>
                </button>
              </li>
            </>
          ) : (
            <li className="nav-item">
              <Link to="/login" className="nav-link d-flex align-items-center">
                <FaSignInAlt className="me-1" />
                <span className="ms-1">{t('app.nav.login')}</span>
              </Link>
            </li>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Header;