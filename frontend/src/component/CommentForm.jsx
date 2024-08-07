import React, {useState} from 'react';
import {fetchWithAuth} from "../api/api";

const CommentForm = ({articleId}) => {
    const [content, setContent] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!content.trim()) return;

        fetchWithAuth(`/article/${articleId}/comment`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "content": content
            }),
        })
            .then(() => {
                setContent('');
            });
    };

    return (
        <form onSubmit={handleSubmit} className="comment-form">
            <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="댓글을 입력하세요"
                className="comment-textarea"
            />
            <button type="submit" className="submit-comment">댓글 작성</button>
        </form>
    );
};

export default CommentForm;