import React from 'react';
import { Link } from "react-router-dom";
import { useAuth } from '../../authContext/AuthContext';

const Header = () => {

  const { token, logout } = useAuth();
  //const navigate = useNavigate();

  return (
    <div>
        <nav className="navbar navbar-expand navbar-dark bg-dark">
            <a className="navbar-brand" href="/accounts">Accounts Management System:</a>
            <div className="navbar-nav mr-auto">
              <li className="nav-item">
                <Link to={"/accounts"} className="nav-link">
                  Accounts List
                </Link>
              </li>
              <li className="nav-item">
                <Link to={"/add-account"} className="nav-link">
                  Add new 
                </Link>
              </li>
              <li className="nav-item">
                <Link to={"/accounts/search"} className="nav-link">
                  Search 
                </Link>
              </li>
              <li className="nav-item">
                <Link to={"/"} className="nav-link">
                  Home
                </Link>
              </li>
              <li className="nav-item">
                <Link to={"/users"} className="nav-link">
                  UserList
                </Link>
              </li>
            </div>

            <div className="navbar-nav ms-auto">
          {token ? (
            <>
              <li className="nav-item">
                <Link to={"/settings"} className="nav-link">
                  Settings
                </Link>
              </li>
              <li className="nav-item">
                <button className="nav-link btn btn-link" onClick={logout}>
                  Logout
                </button>
              </li>
            </>
          ) : (
            <li className="nav-item">
              <Link to={"/login"} className="nav-link">
                Login
              </Link>
            </li>
          )}
        </div>
      </nav>
    </div>
  );
};

export default Header;
