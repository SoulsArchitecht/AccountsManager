import bgImage from '../../assets/images/main_bg.jpg';
import "./HomePage.css";

function HomePage() {
    return (
        <div className="home-page">
            <div className="home-image-container">
                <img src={bgImage} alt="main_bg_picture" className="home-image"/>
            </div>
        </div>
    );
}

export default HomePage;