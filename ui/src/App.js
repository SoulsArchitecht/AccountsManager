import logo from './logo.svg';
import './App.css';
import AccountList from './components/accountList/AccountList';
import Header from './components/header/Header';
import Footer from './components/footer/Footer';
import {BrowserRouter, Routes, Route} from 'react-router-dom';

function App() {
  return (
    <div className="container-xxl">
      <BrowserRouter>
        <Header/>
          <Routes>
            <Route path='api/v1/' element = {<AccountList/>}></Route>
            <Route path='api/v1/accounts' element = {<AccountList/>}></Route>
          </Routes>
        <AccountList/>
        <Footer/>
      </BrowserRouter>
    </div>
  );
}

export default App;
