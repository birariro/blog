package com.birairo.blog.comment.controller;

record ModifyCommentRequest(
        String author,
        String content
) {
}
