import React, {useCallback, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import config from "../api/config";
import {fetchWithAuth} from "../api/api";
import {Prism as SyntaxHighlighter} from "react-syntax-highlighter";

const CreateArticle = () => {
    const [title, setTitle] = useState('');
    const [tags, setTags] = useState([]);
    const [content, setContent] = useState('');
    const [currentTag, setCurrentTag] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        fetchWithAuth(`/article`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({title, content, tags}),
        })
            .then(response => {
                if (response.status === 201) {
                    navigate('/')
                } else {
                    throw new Error(`요청 실패: 상태 코드 ${response.status}`);
                }
            });
    };

    const handleTagKeyDown = (e) => {
        if (e.key === 'Enter' && currentTag.trim()) {
            e.preventDefault();
            setTags([...tags, currentTag.trim()]);
            setCurrentTag('');
        } else if (currentTag.trim().length === 0 && e.key === 'Backspace') {
            e.preventDefault();
            setTags((tags) => tags.slice(0, -1));
            setCurrentTag('');
        }
    };

    const handleDrop = useCallback((e) => {
        e.preventDefault();
        const file = e.dataTransfer.files[0];
        if (file && file.type.startsWith('image/')) {
            const formData = new FormData();
            formData.append('file', file);

            fetch(`${config.API_BASE_URL}/storage/upload`, {
                method: 'POST',
                body: formData,
            })
                .then(response => response.json())
                .then(data => {
                    const imageMarkdown = `![](${data.url})`;
                    setContent(prevContent => prevContent + '\n' + imageMarkdown + '\n');
                })
                .catch(error => console.error('Error uploading image:', error));
        }
    }, []);

    const handleDragOver = useCallback((e) => {
        e.preventDefault();
    }, []);

    return (
        <div className="container">
            <form onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="title">제목</label>
                    <input
                        id="title"
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="멋진 제목을 입력해주세요"
                        required
                        className="title-input"
                    />
                </div>
                <div className="input-group">
                    <label htmlFor="tags">태그</label>
                    <div className="tags-input">
                        {tags.map((tag, index) => (
                            <span key={index} className="tag">#{tag}</span>
                        ))}
                        <input
                            id="tags"
                            type="text"
                            value={currentTag}
                            onChange={(e) => setCurrentTag(e.target.value)}
                            onKeyDown={handleTagKeyDown}
                            placeholder="해시태그 입력 후 Enter"
                        />
                    </div>
                </div>
                <div className="input-group">
                    <label htmlFor="content">내용</label>
                    <div className="content-preview-container">
                        <textarea
                            id="content"
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            onDrop={handleDrop}
                            onDragOver={handleDragOver}
                            placeholder="내용을 Markdown 형식으로 작성해주세요. 이미지는 드래그 앤 드롭으로 추가할 수 있습니다."
                            required
                        />
                        <div className="preview">
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
                                }}>{content}</ReactMarkdown>
                        </div>
                    </div>
                </div>
                <button type="submit" className="submit-button">게시하기</button>
            </form>
        </div>
    );
};

export default CreateArticle;


