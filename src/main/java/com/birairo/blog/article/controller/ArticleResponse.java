package com.birairo.blog.article.controller;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.domain.Tag;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ArticleResponse {
    private UUID id;
    private String title;
    private String content;
    private List<String> tags;
    private List<CommentResponse> comments;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle().getValue();
        this.content = article.getContent().getValue();
        this.tags = article.getTags()
                .stream().map(Tag::getName).toList();
        this.comments = article.getComments()
                .stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getAuthor().getValue(), comment.getContent().getValue()))
                .toList();
    }
}
