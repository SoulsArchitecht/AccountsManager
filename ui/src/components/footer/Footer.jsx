import React, { useEffect, useState } from 'react'
import { fetchAppInfo } from '../../services/AppInfoService';
import './footer.css'

const Footer = () => {

  const [appInfo, setAppInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  
  
  useEffect(() => {
    fetchAppInfo()
      .then(data => {
        setAppInfo(data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Failed to load app info', err);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <footer className="footer">Загрузка...</footer>;
  }

  return (
    <div>
      <footer className="footer">
        <div className="contact-info">
          {appInfo.email && (
            <a href={appInfo.email} target="_blank" rel="noopener noreferrer">
              📧 Email
            </a>
          )}
          {appInfo.phone && (
            <>
              {appInfo.email && ' | '}
              <a href={appInfo.phone}>☎️ Звонок</a>
            </>
          )}
          {appInfo.telegram && (
            <>
              {(appInfo.email || appInfo.phone) && ' | '}
              <a href={appInfo.telegram} target="_blank" rel="noopener noreferrer">
                📱 Telegram
              </a>
            </>
          )}
        </div>

        <div className="version-info">
          Версия: {appInfo.versionNumber}
          {appInfo.versionStatus === 'TEST' && (
            <span className="test-badge">ТЕСТОВАЯ ВЕРСИЯ</span>
          )}
        </div>

        <div className="copyright">
          All rights reserved ©2024 by Serge Shibko
        </div>
      </footer>
    </div>
  )
}

export default Footer;