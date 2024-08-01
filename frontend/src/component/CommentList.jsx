import React, {useEffect, useState} from 'react';
import config from "../api/config";
import ResponseToJson from "../api/ApiWapper";

const CommentList = ({articleId}) => {
    const [comments, setComments] = useState([]);

    useEffect(() => {
        fetch(`${config.API_BASE_URL}/article/${articleId}/comment`)
            .then(response => ResponseToJson(response))
            .then(data => {
                data && setComments(data)
            })
            .catch(error => {
                console.error('데이터를 가져오는 중 에러 발생:', error);
            });
    }, [articleId]);

    return (
        <div className="comment-list">
            <h3>댓글</h3>
            {comments.map(comment => (
                <div key={comment.id} className="comment">
                    <p>{comment.content}</p>
                </div>
            ))}
        </div>
    );
};

export default CommentList;