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
const CACHE_VERSION = 'v1'; 

const fetchMessages = async () => {
  const cached = localStorage.getItem(CACHE_KEY);
  if (cached) {
    const { version, data, timestamp } = JSON.parse(cached);
    if (version === CACHE_VERSION && Date.now() - timestamp < 24 * 60 * 60 * 1000) {
      return data;
    }
  }

  try {
    const response = await axios.get('/localization/messages');
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

  useEffect(() => {
    fetchMessages()
      .then(setMessages)
      .finally(() => setLoading(false));
  }, []);

  const t = (key) => messages[key] || key;

  return (
    <LocalizationContext.Provider value={{ t, loading }}>
      {children}
    </LocalizationContext.Provider>
  );
};