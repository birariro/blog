package com.birairo.blog.aggregate.service;

import java.util.UUID;

public interface ArticleLoadCount {
    long loadArticleCount(UUID id);
}
