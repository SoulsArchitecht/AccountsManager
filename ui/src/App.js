import logo from './logo.svg';
import './App.css';
import AccountList from './components/accountList/AccountList';
import Header from './components/header/Header';
import Footer from './components/footer/Footer';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import AddAccount from './components/addAccount/AddAccount';
import SearchPanel from './components/searchPanel/SearchPanel';
import HomePage from './components/homePage/HomePage'

function App() {
  return (
    <div className="container-xxl">
      <BrowserRouter>
        <Header/>
        {/* <HomePage/> */}
        {/* <SearchPanel/> */}
          <Routes>
            <Route path='/accounts/search' element = {<SearchPanel/>}> </Route>
            <Route path='/' element = {<HomePage/>}></Route>
            <Route path='/accounts' element = {<AccountList/>}></Route>
            <Route path='/add-account' element = {<AddAccount/>}></Route>
            <Route path='/edit-account/:id' element = {<AddAccount/>}></Route>
          </Routes>
        <Footer/>
      </BrowserRouter>
    </div>
  );
}

export default App;
