import React from 'react';
import { Link } from 'react-router-dom';

const Card = ({ article }) => (
    <Link to={`/article/${article.id}`} className="card">
        <h2>{article.title}</h2>
    </Link>
);

export default Card;