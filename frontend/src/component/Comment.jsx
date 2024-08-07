// Comment.jsx
import React, {useState} from 'react';

const Comment = ({comment, articleId, onActionComment, isNestedComment}) => {
    const [commentContent, setCommentContent] = useState('');
    const [showCommentForm, setShowCommentForm] = useState(false);

    const handleReply = () => {
        onActionComment(comment.id, commentContent);
        setCommentContent('');
        setShowCommentForm(false);
    };

    return (
        <div className={`comment ${isNestedComment ? 'nested-comment' : ''}`}>
            <div className="comment-header">
                <span className="comment-author">{comment.author}</span>
                <span className="comment-date">{new Date(comment.createdAt).toLocaleDateString()}</span>
            </div>
            <p className="comment-content">{comment.content}</p>
            {!isNestedComment && (
                <button className="reply-button" onClick={() => setShowCommentForm(!showCommentForm)}>
                    {showCommentForm ? '취소' : '답글'}
                </button>
            )}
            {showCommentForm && (
                <div className="reply-form">
                    <textarea
                        value={commentContent}
                        onChange={(e) => setCommentContent(e.target.value)}
                        placeholder="답글을 입력하세요"
                        className="reply-textarea"
                    />
                    <button onClick={handleReply} className="submit-reply">답글 작성</button>
                </div>
            )}
            {comment.comments && comment.comments.length > 0 && (
                <div className="replies">
                    {comment.comments.map(reply => (
                        <Comment key={reply.id} comment={reply} articleId={articleId} onActionComment={onActionComment} isNestedComment={true}/>
                    ))}
                </div>
            )}
        </div>
    );
};
export default Comment;