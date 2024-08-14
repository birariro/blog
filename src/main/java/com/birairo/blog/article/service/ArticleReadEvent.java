package com.birairo.blog.article.service;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.event.BaseEvent;

import java.util.UUID;

public class ArticleReadEvent extends BaseEvent {
    private final Article article;

    public ArticleReadEvent(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }
}
