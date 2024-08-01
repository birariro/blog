import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import config from "../api/config";

const CreateArticle = () => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [tags, setTags] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch(`${config.API_BASE_URL}/article`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "title": title,
                "content": content,
                "tags": tags.split(',')
            }),
        })
            .then(response => {
                if (response.status === 201) {
                    navigate('/')
                } else {
                    throw new Error(`요청 실패: 상태 코드 ${response.status}`);
                }
            });
    };

    return (
        <div className="create-article">
            <h1>새로운 게시글 작성</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="제목"
                    required
                />

                <div className="content-preview-container">
                  <textarea
                      value={content}
                      onChange={(e) => setContent(e.target.value)}
                      placeholder="내용 (Markdown 형식으로 작성)"
                      required
                  />
                    <div className="preview">
                        <ReactMarkdown>{content}</ReactMarkdown>
                    </div>
                </div>

                <input
                    type="text"
                    value={tags}
                    onChange={(e) => setTags(e.target.value)}
                    placeholder="해시태그 (쉼표로 구분)"
                />
                <button type="submit">저장</button>
            </form>
        </div>
    );
};

export default CreateArticle;