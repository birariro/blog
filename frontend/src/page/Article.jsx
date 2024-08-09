import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import CommentList from '../component/CommentList';
import CommentForm from '../component/CommentForm';
import ReactMarkdown from 'react-markdown';
import {fetchWithAuth} from "../api/api";

const Article = () => {
    const [article, setArticle] = useState(null);
    const {id} = useParams();
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        fetchWithAuth(`/article/${id}`)
            .then(response => response.json())
            .then(data => setArticle(data));

        // Check if JWT token exists in localStorage
        const token = localStorage.getItem('jwt');
        setIsLoggedIn(!!token);
    }, [id]);

    const handleEdit = () => {
        navigate(`/edit-article/${id}`);
    };

    if (!article) return <div className="loading">Loading...</div>;

    return (
        <div className="container">
            <article className="article">
                <header className="article-header">
                    <h1 className="article-title">{article.title}</h1>
                    <div className="article-meta">
                        <span className="article-date">{new Date(article.createdAt).toLocaleDateString()}</span>
                        <div className="article-tags">
                            {article.tags && article.tags.map((tag, index) => (
                                <span key={index} className="article-tag">#{tag}</span>
                            ))}
                        </div>
                    </div>
                </header>
                <div className="article-content">
                    <ReactMarkdown>{article.content}</ReactMarkdown>
                </div>

                {isLoggedIn && (
                    <button onClick={handleEdit} className="edit-button">
                        수정하기
                    </button>
                )}
            </article>
            <section className="article-comments">
                <h2 className="comments-title">Comments</h2>
                <CommentList articleId={id}/>
                <CommentForm articleId={id}/>
            </section>
        </div>
    );
};

export default Article;