import axios from 'axios';

const CACHE_KEY = 'app_translations';
const CACHE_VERSION = 'v2';

export const fetchMessages = async() => {
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
        console.warn('Failed to fetch translations, falling back to cache or empty');
        return {};
    }
};