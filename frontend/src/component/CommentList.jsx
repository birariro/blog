import React, {useEffect, useState} from 'react';
import Comment from './Comment';
import ResponseToJson from "../api/ApiWapper";
import {fetchWithAuth} from "../api/api";

const CommentList = ({articleId}) => {
    const [comments, setComments] = useState([]);

    useEffect(() => {
        fetchComments();
    }, [articleId]);

    const fetchComments = () => {
        fetchWithAuth(`/article/${articleId}/comment`)
            .then(response => ResponseToJson(response))
            .then(data => {
                if (data.comments) setComments(data.comments)
            })
            .catch(error => {
                console.error('데이터를 가져오는 중 에러 발생:', error);
            });
    };

    const saveComment = (commentId, content) => {
        fetchWithAuth(`/comment/${commentId}/comment`, {
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