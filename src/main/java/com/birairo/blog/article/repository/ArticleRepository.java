package com.birairo.blog.article.repository;

import com.birairo.blog.article.domain.Article;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends Repository<Article, UUID> {
    Article save(Article article);
    Optional<Article> findById(UUID id);

}
