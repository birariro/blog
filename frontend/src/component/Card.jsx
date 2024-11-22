import React from 'react';
import {Link} from 'react-router-dom';

const Card = ({article}) => (
    <Link to={`/article/${article.id}`} className="card">
        {article.thumbnail && (
            <img
                src={article.thumbnail}
                alt={article.title}
                className="card-image"
            />
        )}
        <div className="card-content">
            <h2>{article.title}</h2>
            <p>{article.summary}...</p>
            <div className="tags">
                {article.tags && article.tags.map((tag, index) => (
                    <span key={index} className="tag">#{tag}</span>
                ))}
            </div>
        </div>
    </Link>
);

export default Card;