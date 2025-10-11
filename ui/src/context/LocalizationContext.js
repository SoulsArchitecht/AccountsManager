import React, { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';

const LocalizationContext = createContext();

export const useLocalization = () => {
  const context = useContext(LocalizationContext);
  if (!context) {
    throw new Error('useLocalization must be used within LocalizationProvider');
  }
  return context;
};

const getBrowserLanguage = () => {
  const lang = (navigator.language || navigator.userLanguage || 'en').split('-')[0];
  return ['ru', 'en'].includes(lang) ? lang : 'en';
};

export const LocalizationProvider = ({ children }) => {
  const [messages, setMessages] = useState({});
  const [loading, setLoading] = useState(true);
  const [currentLang, setCurrentLang] = useState('en');

  const loadTranslations = async (lang) => {
    setLoading(true);
    try {
      const response = await axios.get('/localization/messages', {
        headers: { 'Accept-Language': lang }
      });
      setMessages(response.data);
      setCurrentLang(lang);
    } catch (error) {
      console.error('Failed to load translations', error);
      setMessages({});
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const savedLang = localStorage.getItem('app_language') || getBrowserLanguage();
    loadTranslations(savedLang);
  }, []);

  const changeLanguage = (lang) => {
    localStorage.setItem('app_language', lang);
    loadTranslations(lang);
  };

  const t = (key, ...args) => {
    let str = messages[key] || key;
    args.forEach((arg, i) => {
      str = str.replace(`{${i}}`, arg);
    });
    return str;
  };

  return (
    <LocalizationContext.Provider value={{ t, loading, currentLang, changeLanguage }}>
      {children}
    </LocalizationContext.Provider>
  );
};