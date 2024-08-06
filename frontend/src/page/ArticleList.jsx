import React, {useEffect, useState} from 'react';
import Card from '../component/Card';
import ResponseToJson from "../api/ApiWapper";
import {fetchWithAuth} from "../api/api";

const ArticleList = () => {
    const [articles, setArticles] = useState([]);

    useEffect(() => {
        fetchWithAuth(`/article`)
            .then(response => ResponseToJson(response))
            .then(data => setArticles(data));
    }, []);

    return (
        <div className="article-list-container">
            <div className="article-list-header">
                {/*<h1>블로그 게시글</h1>*/}
            </div>
            <div className="article-list">
                {articles.map(article => (
                    <Card key={article.id} article={article}/>
                ))}
            </div>
        </div>
    );
};

export default ArticleList;