package com.birairo.blog.article.controller;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.domain.Tag;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class ArticleResponse {
    private UUID id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<String> tags;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle().getValue();
        this.content = article.getContent().getValue();
        this.createdAt = article.getCreatedAt();
        this.tags = article.getTags()
                .stream().map(Tag::getName).toList();
    }
}
