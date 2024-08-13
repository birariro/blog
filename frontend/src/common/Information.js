export const isLogin = () => {
    const token = localStorage.getItem('jwt');
    return token != undefined;
};

export const logout = () => {
    localStorage.removeItem('jwt');
};
export const login = (token) => {
    
    if (isLogin()) {
        localStorage.removeItem('jwt');
    }
    localStorage.setItem('jwt', token);
}