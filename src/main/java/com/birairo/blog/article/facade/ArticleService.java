package com.birairo.blog.article.facade;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.domain.Tag;
import com.birairo.blog.article.usecase.ArticleCreator;
import com.birairo.blog.article.usecase.ArticleLoader;
import com.birairo.blog.article.usecase.ArticleValidator;
import com.birairo.blog.article.vo.Author;
import com.birairo.blog.article.vo.Content;
import com.birairo.blog.article.vo.Title;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleLoader articleLoader;
    private final ArticleCreator articleCreator;
    private final ArticleValidator articleValidator;

    @Transactional(readOnly = true)
    public List<ArticleHeader> findArticleHeader() {

        List<Article> articles = articleLoader.loadArticles();
        return articleToArticleHeader(articles);
    }

    @Transactional(readOnly = true)
    public Article findArticle(final UUID id) {
        return articleLoader.loadArticleAndCommentsAndTags(id);
    }

    private List<ArticleHeader> articleToArticleHeader(List<Article> articles) {
        return articles.stream()
                .map(article -> new ArticleHeader(article.getId(), article.getTitle().getValue()))
                .toList();
    }

    @Transactional
    public void saveArticle(final String title, final String content, final List<String> tags) {


        Title _title = Title.of(title);
        Content _content = Content.of(content);
        List<Tag> _tags = tags.stream().map(Tag::new).toList();

        articleValidator.validTitle(_title);
        articleCreator.createArticle(_title, _content, _tags);
    }

    @Transactional
    public void saveArticle(final UUID id, final String title, final String content, List<String> tags) {

        Title _title = Title.of(title);
        Content _content = Content.of(content);
        List<Tag> _tags = tags.stream().map(Tag::new).toList();

        Article article = articleLoader.loadArticle(id);
        if (!article.getTitle().equals(_title)) {
            articleValidator.validTitle(_title);
        }

        article.modify(_title, _content, _tags);
    }

    @Transactional
    public void saveComment(final UUID id, final String author, final String content) {

        Author _author = Author.of(author);
        Content _content = Content.of(content);

        Article article = articleLoader.loadArticle(id);
        article.newComment(_author, _content);
    }
}
