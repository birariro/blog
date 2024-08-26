import config from './config';

export const fetchWithAuth = async (url, options = {}) => {

    const response = await fetch(`${config.API_BASE_URL}${url}`, {
        ...options
    });

    return response;
};