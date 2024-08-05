import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
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
                <h1>게시글</h1>
                <Link to="/create" className="create-button">새로운 게시글 작성</Link>
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