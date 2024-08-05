package com.birairo.blog.article.service;

import com.birairo.blog.vo.Title;

import java.util.UUID;

public interface ArticleValidator {
    boolean validTitle(Title title);
    boolean existsArticle(UUID id);
}
