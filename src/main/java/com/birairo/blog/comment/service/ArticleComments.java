package com.birairo.blog.comment.service;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ArticleComments {

    private final UUID articleId;
    private List<ParentComment> comments;

    public ArticleComments(UUID articleId) {
        this.articleId = articleId;
        this.comments = new ArrayList<>();

    }

    public void addParentComment(ParentComment parentComment) {
        this.comments.add(parentComment);
    }
}

