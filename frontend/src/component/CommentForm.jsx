import React, {useState} from 'react';
import config from "../api/config";

const CommentForm = ({articleId}) => {
    const [content, setContent] = useState('');
    const [author, setAuthor] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        fetch(`${config.API_BASE_URL}/article/${articleId}/comment`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "author": author,
                "content": content
            }),
        })
            .then(() => {
                setAuthor('')
                setContent('');
            });
    };

    return (
        <form onSubmit={handleSubmit} className="comment-form">
            <input
                value={author}
                onChange={(e) => setAuthor(e.target.value)}
                placeholder="이름을 입력하세요"
            />
            <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="댓글을 입력하세요"
            />
            <button type="submit">댓글 작성</button>
        </form>
    );
};

export default CommentForm;