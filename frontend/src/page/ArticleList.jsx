import React, {useEffect, useState} from 'react';
import Masonry from 'react-masonry-css';
import Card from '../component/Card';
import {fetchArticles} from "../api";
import Loading from "../component/Loading";

const ArticleList = () => {
    const [articles, setArticles] = useState([]);

    const breakpointColumns = {
        default: 3,
        1100: 2,
        700: 1
    };

    useEffect(() => {
        fetchArticles()
            .then(data => setArticles(data));
    }, []);

    if (!articles) return <Loading/>;

    return (
        <div className="article-list-container">
            <Masonry
                breakpointCols={breakpointColumns}
                className="masonry-grid"
                columnClassName="masonry-grid_column"
            >
                {articles && articles.map(article => (
                    <div key={article.id} className="card-wrapper">
                        <Card article={article}/>
                    </div>
                ))}
            </Masonry>
        </div>
    );
};

export default ArticleList;