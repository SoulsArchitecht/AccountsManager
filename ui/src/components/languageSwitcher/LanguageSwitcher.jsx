import React from 'react';
import { useLocalization } from '../../context/LocalizationContext';

const LanguageSwitcher = () => {
    const { currentLang, changeLanguage } = useLocalization();


    return (
        <div className="language-switcher">
            <button
                onClick={() => changeLanguage('en')}
                disabled={currentLang === 'en'}
                title="English"
            >
               🇬🇧 
            </button>
            <button
                onClick={() => changeLanguage('ru')}
                disabled={currentLang === 'ru'}
                title="Русский"
            >
               🇷🇺     
            </button>    
        </div>
    );
};

export default LanguageSwitcher;