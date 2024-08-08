package com.birairo.blog.member.service;

import com.birairo.blog.member.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class NestedComment {
    private final UUID id;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;

    public NestedComment(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getAuthor().getNickname().getValue();
        this.content = comment.getContent().getValue();
        this.createdAt = comment.getCreatedAt();
    }
}