import { Navigate, Outlet} from 'react-router-dom';
import { useAuth } from '../authContext/AuthContext';

const PrivateRoute = () => {
    const { token } = useAuth();
    return token ? <Outlet /> : <Navigate to="/login" />
};

const PublicRoute = () => {
    const { token } = useAuth();
    return !token ? <Outlet /> : <Navigate to="/" />;
};

export { PrivateRoute, PublicRoute };