import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import ArticleList from './page/ArticleList';
import Article from './page/Article';
import CreateArticle from "./page/CreateArticle";

const App = () => {
    return (
        <Router>
            <div className="app">
                <Routes>
                    <Route exact path="/" element={<ArticleList/>}/>
                    <Route path="/article/:id" element={<Article/>}/>
                    <Route path="/create" element={<CreateArticle/>}/>
                </Routes>
            </div>
        </Router>
    );
};

export default App;