import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import CommentList from '../component/CommentList';
import CommentForm from '../component/CommentForm';
import ReactMarkdown from 'react-markdown';
import config from "../api/config";

const Article = () => {
    const [article, setArticle] = useState(null);
    const {id} = useParams();

    useEffect(() => {
        fetch(`${config.API_BASE_URL}/article/${id}`)
            .then(response => response.json())
            .then(data => setArticle(data));
    }, [id]);

    if (!article) return <div>Loading...</div>;

    return (
        <div className="article">
            <h1>{article.title}</h1>

            <ReactMarkdown>{`# ${article.content}`}</ReactMarkdown>

            <div className="tags">
                {article.tags && article.tags.map((tag, index) => (
                    <span key={index} className="tag">#{tag}</span>
                ))}
            </div>
            <CommentList articleId={id}/>
            <CommentForm articleId={id}/>
        </div>
    );
};

export default Article;