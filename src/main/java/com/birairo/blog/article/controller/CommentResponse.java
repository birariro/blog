package com.birairo.blog.article.controller;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CommentResponse {
    private UUID id;
    private String author;
    private String content;

    public CommentResponse(UUID id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }
}
