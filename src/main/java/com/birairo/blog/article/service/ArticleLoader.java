package com.birairo.blog.article.service;

import com.birairo.blog.article.domain.Article;

import java.util.List;
import java.util.UUID;

public interface ArticleLoader {
    List<ArticleHeader> findArticleHeader();

    Article findArticle(final UUID id);
}
