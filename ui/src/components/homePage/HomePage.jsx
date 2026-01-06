//import bgImage from '../../assets/images/main_bg.jpg';
import "./HomePage.css";
import { useLocalization } from '../../context/LocalizationContext';

function HomePage() {

 const { t } = useLocalization();


    return (
        <div className="home-hero">
        <div className="home-overlay">
            <div className="home-content text-center text-white">
            <p className="home-description">{t('home.description')}</p>
            <a
                href="/swagger-ui/index.html"
                target="_blank"
                rel="noopener noreferrer"
                className="home-link"
            >
                {t('home.link')}
            </a>
            </div>
        </div>
        </div>
    );
}

export default HomePage;