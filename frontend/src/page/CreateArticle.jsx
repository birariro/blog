import React, {useCallback, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import config from "../api/config";
import {fetchWithAuth} from "../api/api";

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
        <div className="create-article">
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="제목"
                    required
                    className="title-input"
                />
                <div className="tags-input">
                    {tags.map((tag, index) => (
                        <span key={index} className="tag">#{tag}</span>
                    ))}
                    <input
                        type="text"
                        value={currentTag}
                        onChange={(e) => setCurrentTag(e.target.value)}
                        onKeyDown={handleTagKeyDown}
                        placeholder="해시태그 입력 후 Enter"
                    />
                </div>
                <div className="content-preview-container">
          <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              onDrop={handleDrop}
              onDragOver={handleDragOver}
              placeholder="내용 (Markdown 형식으로 작성). 이미지를 드래그 앤 드롭하세요."
              required
          />
                    <div className="preview">
                        <ReactMarkdown>{content}</ReactMarkdown>
                    </div>
                </div>
                <button type="submit" className="submit-button">저장</button>
            </form>
        </div>
    );
};

export default CreateArticle;