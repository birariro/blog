import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Link, Route, Routes} from 'react-router-dom';

import ArticleList from './page/ArticleList';
import Article from './page/Article';
import CreateArticle from "./page/CreateArticle";
import LoginForm from "./page/LoginForm";
import EditArticle from "./page/EditArticle";

const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('jwt');
        setIsLoggedIn(!!token);
    }, []);

    const handleLogin = () => {
        setIsLoggedIn(true);
    };

    const handleLogout = () => {
        localStorage.removeItem('jwt');
        setIsLoggedIn(false);
    };

    return (
        <Router>
            <div className="board">
                <div className="app">
                    <header>
                        {isLoggedIn ? (
                            <>
                                <Link to="/create" className="create-button">새로운 게시글 작성</Link>
                                <button onClick={handleLogout} className="logout-button">로그아웃</button>
                            </>
                        ) : (
                            <Link to="/login" className="login-button">로그인</Link>
                        )}
                    </header>
                    <Routes>
                        <Route exact path="/" element={<ArticleList/>}/>
                        <Route path="/article/:id" element={<Article/>}/>
                        <Route path="/create" element={<CreateArticle/>}/>
                        <Route path="/login" element={<LoginForm onLogin={handleLogin}/>}/>
                        <Route path="/edit-article/:id" element={<EditArticle/>}/>


                    </Routes>
                </div>
            </div>
        </Router>
    );
};

export default App;