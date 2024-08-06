import React from 'react';
import {Link} from 'react-router-dom';

const Card = ({article}) => (
    <Link to={`/article/${article.id}`} className="card">
        <h2>{article.title}</h2>
        {/*<p>{article.content.substring(0, 100)}...</p>*/}
        <p>{article.title.substring(0, 100)}...</p>
        <div className="tags">
            {article.tags && article.tags.map((tag, index) => (
                <span key={index} className="tag">#{tag}</span>
            ))}
        </div>
    </Link>
);

export default Card;