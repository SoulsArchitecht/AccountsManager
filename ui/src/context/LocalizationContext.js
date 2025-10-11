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

const CACHE_KEY = 'app_translations';
const CACHE_VERSION = 'v2';
const LANG_KEY = 'app_language'; 

const fetchMessages = async (lang) => {
  const cached = localStorage.getItem(CACHE_KEY);
  if (cached) {
    const { version, data, timestamp } = JSON.parse(cached);
    if (version === CACHE_VERSION && Date.now() - timestamp < 24 * 60 * 60 * 1000) {
      return data;
    }
  }

  try {
    const response = await axios.get('/localization/messages', {
      headers: { 'Accept-Language': lang}
    });
    const payload = {
      version: CACHE_VERSION,
      data: response.data,
      timestamp: Date.now()
    };
    localStorage.setItem(CACHE_KEY, JSON.stringify(payload));
    return response.data;
  } catch (error) {
    console.warn('Failed to load translations, falling back to empty');
    return {};
  }
};

export const LocalizationProvider = ({ children }) => {
  const [messages, setMessages] = useState({});
  const [loading, setLoading] = useState(true);
  const [currentLang, setCurrentLang] = useState('en');

  const loadTranslations = async (lang) => {
    setLoading(true);
    const data = await fetchMessages(lang);
    setMessages(data);
    setCurrentLang(lang);
    localStorage.setItem(LANG_KEY, lang);
    setLoading(false);
  }

  useEffect(() => {
    const savedLang = localStorage.getItem(LANG_KEY) || 'en';
    loadTranslations(savedLang);
  }, []);

  const changeLanguage = (lang) => {
    loadTranslations(lang);
  }

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