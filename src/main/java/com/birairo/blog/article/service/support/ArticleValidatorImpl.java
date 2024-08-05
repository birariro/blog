package com.birairo.blog.article.service.support;

import com.birairo.blog.article.service.ArticleValidator;
import com.birairo.blog.article.service.support.repository.ArticleRepository;
import com.birairo.blog.vo.Title;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class ArticleValidatorImpl implements ArticleValidator {
    private final ArticleRepository repository;

    @Transactional(readOnly = true)
    public boolean validTitle(Title title) {
        return repository.existsByTitle(title);
    }

    @Override
    public boolean existsArticle(UUID id) {
        return repository.existsById(id);
    }
}
