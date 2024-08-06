package com.birairo.blog.article.service.support.repository;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.vo.Title;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends Repository<Article, UUID> {
    Article save(Article article);

    Optional<Article> findById(UUID id);

    @Query("select distinct a from Article a " +
            "left join fetch a.tags " +
            "where a.id =:id")
    Optional<Article> findByIdAndTags(UUID id);

    boolean existsByTitle(Title title);

    boolean existsById(UUID id);

    List<Article> findAll();
}
