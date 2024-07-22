package com.birairo.blog.article.domain;

import com.birairo.blog.article.repository.ArticleRepository;
import com.birairo.blog.article.vo.Author;
import com.birairo.blog.article.vo.Content;
import com.birairo.blog.article.vo.Title;
import com.birairo.blog.env.DomainTest;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DomainTest
class ArticleTest {

    @Autowired
    ArticleRepository repository;

    @Autowired
    EntityManager entityManager;

    private Article firstArticle;

    @BeforeEach
    void init() {
        this.firstArticle = new Article(Title.of("title"), Content.of("content"), List.of());
        this.firstArticle = repository.save(this.firstArticle);
        assertThat(firstArticle)
                .isEqualTo(repository.findById(firstArticle.getId()).get());
    }

    @Test
    void should_markdown_then_article_content() {

        String markdown = """
                ### sub title1
                   
                ```java
                void test(){
                }
                ```
                1. list item1
                2. list item2
                                
                """;
        Article article = new Article(Title.of("title"), Content.of(markdown), List.of());
        System.out.println("article.getContent() = " + article.getContent().getValue());
        assertThat(article.getContent())
                .isEqualTo(Content.of(markdown));
    }

    @Test
    void should_add_comment() {

        firstArticle.newComment(Author.of("comment author"), Content.of("comment content"));
        assertThat(firstArticle.getComments().size()).isEqualTo(1);
    }

    @Test
    void should_tag_article() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Article tagArticle = new Article(Title.of("title"), Content.of("content"), List.of(tag1, tag2));

        List<Tag> tags = tagArticle.getTags();
        System.out.println("tags = " + tags);
        Assertions.assertThat(tags)
                .containsExactly(tag1, tag2);
    }
}
