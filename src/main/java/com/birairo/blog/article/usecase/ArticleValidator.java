package com.birairo.blog.article.usecase;

import com.birairo.blog.article.repository.ArticleRepository;
import com.birairo.blog.vo.Title;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleValidator {
    private final ArticleRepository repository;

    @Transactional(readOnly = true)
    public boolean validTitle(Title title) {
        return repository.existsByTitle(title);
    }
}
