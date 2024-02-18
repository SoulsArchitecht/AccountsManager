import React from 'react';
import {Link} from "react-router-dom";

const Header = () => {
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
            </div>
        </nav>
    </div>
  )
}

export default Header
