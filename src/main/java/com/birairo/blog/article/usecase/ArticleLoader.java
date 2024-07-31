package com.birairo.blog.article.usecase;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.repository.ArticleRepository;
import com.birairo.blog.common.NoSuchEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleLoader {

    private final ArticleRepository repository;

    @Transactional(readOnly = true)
    public List<Article> loadArticles() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Article loadArticle(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("not found article"));
    }

    @Transactional(readOnly = true)
    public Article loadArticleAndCommentsAndTags(UUID id) {

        Article article = repository.findByIdAndTags(id)
                .orElseThrow(() -> new NoSuchEntityException("not found article"));
        return article;
    }
}
