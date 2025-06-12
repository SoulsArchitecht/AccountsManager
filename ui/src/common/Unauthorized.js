import React from 'react';
import { useNavigate } from 'react-router-dom';

const Unauthorized = ({ title = "Access Denied", message = "You don't have permission to view this page"}) => {
    const navigate = useNavigate();

    return (
        <div className="container mt-5">
            <div className="card text-center">
                <div className="card-header bg-danger text-white">
                    <h3>{title}</h3>
                </div>
                <div className="card-body">
                    <p className="card-text">{message}</p>
                    <button 
                        onClick={() => navigate('/')}
                        className="btn btn-primary"
                    >
                        Go to Home Page
                    </button>
                </div>
            </div>
        </div>    
    );
};

export default Unauthorized;
