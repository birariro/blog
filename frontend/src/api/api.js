import config from './config';

export const fetchWithAuth = async (url, options = {}) => {
    const token = localStorage.getItem('jwt');

    let headers = {
        ...options.headers,
    };

    if (token != undefined && token.length > 0) {
        headers = {
            ...headers,
            'Authorization': `Bearer ${token}`,
        };
    }

    const response = await fetch(`${config.API_BASE_URL}${url}`, {
        ...options,
        headers,
    });

    if (response.status === 401) {
        localStorage.removeItem('jwt');
        window.location.href = '/login';
        throw new Error('Unauthorized');
    }

    return response;
};