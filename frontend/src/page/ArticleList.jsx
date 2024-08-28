import React, {useEffect, useState} from 'react';
import Card from '../component/Card';
import {fetchArticles} from "../api";

const ArticleList = () => {
    const [articles, setArticles] = useState([]);

    useEffect(() => {
        fetchArticles()
            .then(data => setArticles(data));
    }, []);

    return (
        <div className="article-list-container">
            <div className="article-list-header">
                {/*<h1>블로그 게시글</h1>*/}
            </div>
            <div className="article-list">
                {articles && articles.map(article => (
                    <Card key={article.id} article={article}/>
                ))}
            </div>
        </div>
    );
};

export default ArticleList;