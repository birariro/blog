package com.birairo.blog.article.service;

import java.util.UUID;

public record ArticleHeader(
        UUID id,
        String title
) {
}
