import React, {useCallback, useEffect, useState} from 'react';
import Comment from './Comment';
import {API} from "../api/api";

const CommentList = ({articleId}) => {
    const [comments, setComments] = useState([]);
    const fetchComments = useCallback(() => {
        API(`/article/${articleId}/comment`)
            .then(data => {
                if (data.comments) setComments(data.comments);
            })
            .catch(error => {
                console.error('데이터를 가져오는 중 에러 발생:', error);
            });
    }, [articleId]);

    useEffect(() => {
        fetchComments();
    }, [fetchComments]);


    const saveComment = (commentId, content) => {
        API(`/comment/${commentId}/comment`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "content": content
            }),
        })
            .then(() => {
                fetchComments();
            });
    };

    return (
        <div className="comment-list">
            <h3 className="comment-list-title">댓글</h3>
            {comments.map(comment => (
                <Comment key={comment.id} comment={comment} articleId={articleId} onActionComment={saveComment} isNestedComment={false}/>
            ))}
        </div>
    );
};
export default CommentList;