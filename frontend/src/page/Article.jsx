import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import {Prism as SyntaxHighlighter} from 'react-syntax-highlighter';
import {fetchWithAuth} from "../api/api";

const Article = () => {
    const [article, setArticle] = useState(null);
    const {id} = useParams();

    useEffect(() => {
        fetchWithAuth(`/article/${id}`)
            .then(response => response.json())
            .then(data => setArticle(data));

    }, [id]);


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
                    <ReactMarkdown
                        components={{
                            code({node, inline, className, children, ...props}) {
                                const match = /language-(\w+)/.exec(className || '');
                                return !inline && match ? (
                                    <SyntaxHighlighter
                                        language={match[1]}
                                        PreTag="div"
                                        {...props}
                                    >
                                        {String(children).replace(/\n$/, '')}
                                    </SyntaxHighlighter>
                                ) : (
                                    <code className={className} {...props}>
                                        {children}
                                    </code>
                                );
                            },
                            blockquote({node, children, ...props}) {
                                return (
                                    <blockquote className="blockquote" {...props}>
                                        {children}
                                    </blockquote>
                                );
                            },
                        }}
                    >
                        {article.content}
                    </ReactMarkdown>
                </div>

            </article>
            {/*<section className="article-comments">*/}
            {/*    <h2 className="comments-title">Comments</h2>*/}
            {/*    <CommentList articleId={id}/>*/}
            {/*    <CommentForm articleId={id}/>*/}
            {/*</section>*/}
        </div>
    );
};

export default Article;