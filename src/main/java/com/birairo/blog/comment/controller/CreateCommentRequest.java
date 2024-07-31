package com.birairo.blog.comment.controller;

import java.util.UUID;

record CreateCommentRequest(
        UUID target,
        String author,
        String content
) {
}
