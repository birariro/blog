package com.birairo.blog.comment.service;

import com.birairo.blog.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class NestedComment {
    private final UUID id;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;

    public NestedComment(String author, Comment comment) {
        this.id = comment.getId();
        this.author = author;
        this.content = comment.getContent().getValue();
        this.createdAt = comment.getCreatedAt();
    }
}