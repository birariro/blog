package com.birairo.blog.comment.controller;

import java.util.UUID;

record CreateCommentRequest(
        String author,
        String content
) {
}
