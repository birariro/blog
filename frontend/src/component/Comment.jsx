import React, {useState} from 'react';

const Comment = ({comment, articleId, onActionComment, isNestedComment}) => {

    const [commentContent, setCommentContent] = useState('');
    const [showCommentForm, setShowCommentForm] = useState(!isNestedComment);

    const handleReply = () => {
        onActionComment(comment.id, commentContent);
        setCommentContent('');
        setShowCommentForm(isNestedComment);
    };

    return (
        <div className="comment">
            <p><strong>{comment.author}:</strong> {comment.content}</p>
            {comment.comments && comment.comments.length > 0 && (
                <div className="replies">
                    {comment.comments.map(reply => (
                        <Comment key={reply.id} comment={reply} articleId={articleId} onActionComment={onActionComment} isNestedComment={true}/>
                    ))}
                </div>
            )}
            {showCommentForm && (
                <div className="reply-form">
                    <textarea
                        value={commentContent}
                        onChange={(e) => setCommentContent(e.target.value)}
                        placeholder="답글을 입력하세요"
                    />
                    <button onClick={handleReply}>답글 작성</button>
                </div>
            )}
        </div>
    );
};

export default Comment;