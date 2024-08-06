package com.birairo.blog.article.service.support;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.service.ArticleHeader;
import com.birairo.blog.article.service.ArticleLoader;
import com.birairo.blog.article.service.support.repository.ArticleRepository;
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
class ArticleLoaderImpl implements ArticleLoader {
    private final ArticleRepository repository;

    @Transactional(readOnly = true)
    public List<ArticleHeader> findArticleHeader() {

        List<Article> articles = repository.findAll();
        return articleToArticleHeader(articles);
    }

    @Transactional(readOnly = true)
    public Article findArticle(final UUID id) {
        return repository.findByIdAndTags(id)
                .orElseThrow(() -> new NoSuchEntityException("not found article"));
    }

    private List<ArticleHeader> articleToArticleHeader(List<Article> articles) {
        return articles.stream()
                .map(article -> new ArticleHeader(article.getId(), article.getTitle().getValue()))
                .toList();
    }
}
