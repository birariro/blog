import React, {useEffect, useState} from 'react';
import Comment from './Comment';
import config from "../api/config";
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
                data.comments && setComments(data.comments)
            })
            .catch(error => {
                console.error('데이터를 가져오는 중 에러 발생:', error);
            });
    };

    const saveComment = (commentId, content) => {

        fetch(`${config.API_BASE_URL}/comment/${commentId}/comment`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "author": "author",
                "content": content
            }),
        })
            .then(() => {
                fetchComments();
            });

    };

    return (
        <div className="comment-list">
            <h3>댓글</h3>
            {comments.map(comment => (
                <Comment key={comment.id} comment={comment} articleId={articleId} onActionComment={saveComment} isNestedComment={false}/>
            ))}
        </div>
    );
};

export default CommentList;

