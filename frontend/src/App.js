import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Link, Route, Routes} from 'react-router-dom';

import ArticleList from './page/ArticleList';
import Article from './page/Article';
import CreateArticle from "./page/CreateArticle";
import LoginForm from "./page/LoginForm";
import EditArticle from "./page/EditArticle";
import {isLogin, logout} from "./common/Information";

const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        setIsLoggedIn(isLogin());
    }, []);

    const handleLogin = () => {
        setIsLoggedIn(true);
    };

    const handleLogout = () => {
        logout();
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
                                <Link to="/" className="create-button">홈</Link>
                                <button onClick={handleLogout} className="logout-button">로그아웃</button>
                            </>
                        ) : (
                            <>
                                <Link to="/" className="create-button">홈</Link>
                            </>
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