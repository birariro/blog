package com.birairo.blog.article.service.support;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.domain.Tag;
import com.birairo.blog.article.service.ArticleCreator;
import com.birairo.blog.article.service.ArticleLoader;
import com.birairo.blog.article.service.ArticleModifier;
import com.birairo.blog.article.service.ArticleValidator;
import com.birairo.blog.article.service.support.repository.ArticleRepository;
import com.birairo.blog.vo.Content;
import com.birairo.blog.vo.Title;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class ArticleSaveService implements ArticleCreator, ArticleModifier {
    private final ArticleValidator articleValidator;
    private final ArticleLoader articleLoader;
    private final ArticleRepository repository;


    @Transactional
    public void createArticle(Title title, Content content, List<Tag> tags) {

        articleValidator.validTitle(title);
        repository.save(new Article(title, content, tags));
    }

    @Transactional
    public void modifyArticle(UUID id, Title title, Content content, List<Tag> tags) {

        Article article = articleLoader.findArticle(id);

        if (!article.getTitle().equals(title)) {
            articleValidator.validTitle(title);
        }

        article.modify(title, content, tags);
    }

}
