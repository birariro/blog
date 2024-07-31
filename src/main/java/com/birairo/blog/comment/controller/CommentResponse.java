package com.birairo.blog.comment.controller;

import com.birairo.blog.comment.domain.Comment;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CommentResponse {
    private UUID id;
    private UUID target;
    private String author;
    private String content;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.target = comment.getTarget();
        this.author = comment.getAuthor().getValue();
        this.content = comment.getContent().getValue();
    }
}
