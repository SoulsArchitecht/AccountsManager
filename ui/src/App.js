import logo from './logo.svg';
import './App.css';
import AccountList from './components/accountList/AccountList';
import UserList from './components/userList/UserList';
import Header from './components/header/Header';
import Footer from './components/footer/Footer';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import AddAccount from './components/addAccount/AddAccount';
import SearchPanel from './components/searchPanel/SearchPanel';
import HomePage from './components/homePage/HomePage'
import LoginForm from './components/loginForm/LoginFrom';
import RegisterForm from './components/registerForm/RegisterForm';
import { AuthProvider } from './authContext/AuthContext';
import { PrivateRoute, PublicRoute } from './authRoutes/AuthRoutes';
import UserInfo from './components/userInfo/UserInfo';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
    <div className="container-xxl">
      <BrowserRouter>
        <AuthProvider>
          <Header/>
          {/* <HomePage/> */}
          {/* <SearchPanel/> */}
            <Routes>
              {/* Public routes */}
              <Route element={<PublicRoute />}>
                <Route path="/login" element={<LoginForm />} />
                <Route path="/register" element={<RegisterForm />} />
              </Route>

              {/* Private routes */}
              <Route element={<PrivateRoute />}>
                <Route path='/accounts/search' element={<SearchPanel />} />
                <Route path='/accounts' element={<AccountList />} />
                <Route path='/add-account' element={<AddAccount />} />
                <Route path='/edit-account/:id' element={<AddAccount />} />
                <Route path='/users' element={<UserList />} />
                <Route path='/settings' element={<UserInfo/>} />
              </Route>

              {/* Common routes */}
              <Route path='/' element={<HomePage />} />
            </Routes>
          <Footer/>
        </AuthProvider>
      </BrowserRouter>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
}

export default App;
