import React, { useEffect, useState } from 'react'
import { fetchAppInfo } from '../../services/AppInfoService';
import './footer.css'
import { useLocalization } from '../../context/LocalizationContext';

const Footer = () => {

  const [appInfo, setAppInfo] = useState({
  email: null,
  phone: null,
  telegram: null,
  versionNumber: null,
  versionStatus: null
  });
  const [loading, setLoading] = useState(true);
  const { t } = useLocalization();
  
  
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
    return <footer className="footer">{t('common.loading')}</footer>;
  }

  return (
    <div>
      <footer className="footer">
        <div className="contact-info">
          {appInfo.email && (
            <a href={appInfo.email} target="_blank" rel="noopener noreferrer">
              📧 {t('app.contact.email')}
            </a>
          )}
          {appInfo.phone && (
            <>
              {appInfo.email && ' | '}
              <a href={appInfo.phone}>☎️ {t('app.contact.phone')}</a>
            </>
          )}
          {appInfo.telegram && (
            <>
              {(appInfo.email || appInfo.phone) && ' | '}
              <a href={appInfo.telegram} target="_blank" rel="noopener noreferrer">
                📱 {t('app.contact.telegram')}
              </a>
            </>
          )}
        </div>

        <div className="version-info">
          {t('app.version.number')} {appInfo.versionNumber}
          {appInfo.versionStatus === 'TEST' && (
            <span className="test-badge">{t('app.version.status')}</span>
          )}
        </div>

        <div className="copyright">
          {t('app.version.copyright')}
        </div>
      </footer>
    </div>
  )
}

export default Footer;