import React from 'react';
import {BrowserRouter as Router, Route, Routes, useNavigate} from 'react-router-dom';

import ArticleList from './page/ArticleList';
import Article from './page/Article';


const App = () => {


    const HomeButton = () => {
        const navigate = useNavigate();
        return (
            <img
                src="/logo192.png"
                alt="Home"
                onClick={() => navigate('/')}
                style={{
                    cursor: 'pointer',
                    height: '40px',
                    marginRight: 'auto'
                }}
            />
        );
    };

    return (
        <Router>
            <div className="board">
                <div className="app">
                    <header>
                        <HomeButton/>
                    </header>
                    <Routes>
                        <Route exact path="/" element={<ArticleList/>}/>
                        <Route path="/article/:id" element={<Article/>}/>
                    </Routes>
                </div>
            </div>
        </Router>
    );
};

export default App;