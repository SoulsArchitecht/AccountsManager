import bgImage from '../../assets/images/main_bg.jpg';

function HomePage() {
    return (
        <div>
            <div className="text-center mt-4 mb-4">
                <img src={bgImage} alt="main_bg_picture"/>
            </div>
        </div>
    );
}

export default HomePage;