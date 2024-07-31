package com.birairo.blog.article.controller;

record CreateCommentRequest(
        String author,
        String content
) {
}
