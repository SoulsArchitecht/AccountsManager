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
        <span>
          <div className="contact-info">
            {appInfo.email && <>📧 {appInfo.email}</>}
            {appInfo.phone && <> | ☎️ {appInfo.phone}</>}
            {appInfo.telegram && <> | 📱 {appInfo.telegram}</>} 
          </div>
          <div className="version-info">
            Версия: {appInfo.versionNumber}
            {appInfo.versionStatus === 'TEST' && (
              <span className="test-badge">ТЕСТОВАЯ ВЕРСИЯ</span>
            )}
          </div>
        </span>
        <span>All rights reserved ©2024 by Serge Shibko</span>
      </footer>
    </div>
  )
}

export default Footer;