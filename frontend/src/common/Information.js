import config from "../api/config";

export const isLogin = async () => {
    const token = localStorage.getItem('jwt');
    if (!Boolean(token)) {
        return false;
    }

    try {
        const response = await fetch(`${config.API_BASE_URL}/token/${token}/valud`,
            {
                method: 'GET'
            }
        );
        if (response.ok) {
            const data = await response.json();
            return data.valid;
        } else {
            logout();
        }
    } catch (error) {
        logout();
    }


    return Boolean(token);
};

export const logout = () => {
    localStorage.removeItem('jwt');
};
export const login = (token) => {
    localStorage.setItem('jwt', token);
}