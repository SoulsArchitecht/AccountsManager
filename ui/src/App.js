import logo from './logo.svg';
import './App.css';
import AccountList from './components/accountList/AccountList';
import Header from './components/header/Header';
import Footer from './components/footer/Footer';

function App() {
  return (
    <div className="container-xxl">
      <Header/>
      <AccountList/>
      <Footer/>
    </div>
  );
}

export default App;
