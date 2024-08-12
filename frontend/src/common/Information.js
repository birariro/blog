export const isLogin = () => {
    const token = localStorage.getItem('jwt');
    return token != undefined;
};

export const logout = () => {
    localStorage.removeItem('jwt');
};