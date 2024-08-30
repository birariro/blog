import React from 'react';
import {Route, Routes, useNavigate} from 'react-router-dom';

import ArticleList from './page/ArticleList';
import Article from './page/Article';
import Toggle from "./component/Toggle";
import Party from "./page/Party";

const HomeButton = () => {
    const navigate = useNavigate();
    return (
        <img
            src="/logo192.png"
            alt="Home"
            onClick={() => navigate('/')}
            style={{
                cursor: 'pointer',
                height: '40px'
            }}
        />
    );
};

const App = () => {
    return (
        <div className="board">
            <div className="app">
                <header>
                    <div className="header-left">
                        <HomeButton/>
                    </div>

                    <div className="header-center"></div>

                    <div className="header-right">
                        <Toggle/>
                    </div>
                </header>

                <Routes>
                    <Route exact path="/" element={<ArticleList/>}/>
                    <Route exact path="/party" element={<Party/>}/>
                    <Route path="/article/:id" element={<Article/>}/>
                </Routes>
            </div>
        </div>
    );
};

export default App;
