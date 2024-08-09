package com.birairo.blog.member.service;

import com.birairo.blog.member.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ParentComment {

    private final UUID id;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;
    private List<NestedComment> comments;

    public ParentComment(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getAuthor().getNickname().getValue();
        this.content = comment.getContent().getValue();
        this.createdAt = comment.getCreatedAt();
        this.comments = new ArrayList<>();
    }

    public void addNestedComment(NestedComment nestedComment) {
        this.comments.add(nestedComment);
    }
}