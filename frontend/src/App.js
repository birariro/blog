import React, {useState} from 'react';
import {BrowserRouter as Router, Route, Routes, useNavigate} from 'react-router-dom';

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
    const [showParty, setShowParty] = useState(false);

    return (

        <div className="board">
            <div className="app">

                <header>
                    <div className="header-left">
                        <Router>
                            <HomeButton/>
                        </Router>
                    </div>

                    <div className="header-center"></div>

                    <div className="header-right">
                        <Toggle showParty={showParty} setShowParty={setShowParty}/>
                    </div>
                </header>

                {showParty && (
                    <Party/>
                )}

                {!showParty && (
                    <Router>
                        <Routes>
                            <Route exact path="/" element={<ArticleList/>}/>
                            <Route path="/article/:id" element={<Article/>}/>
                        </Routes>
                    </Router>
                )}

            </div>

        </div>
    );
};

export default App;
