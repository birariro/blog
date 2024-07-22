package com.birairo.blog.article;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.domain.ArticleComment;
import com.birairo.blog.article.repository.ArticleRepository;
import com.birairo.config.P6SpyConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(P6SpyConfig.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleTest {

    @Autowired
    ArticleRepository repository;

    @Autowired
    EntityManager entityManager;

    private Article firstArticle;

    @BeforeEach
    void init(){

        this.firstArticle = new Article("title1", "content1");
        this.firstArticle = repository.save(this.firstArticle);
        entityManager.flush();

        assertThat(firstArticle)
                .isEqualTo(repository.findById(firstArticle.getId()).get());
    }

    @Test
    void should_comments_then_article(){

        Article firstArticle = new Article("title1", "content1");
        this.firstArticle = repository.save(firstArticle);
        entityManager.flush();

        String content1 = "good";
        String content2 = "bad";
        firstArticle.newComment("userName1", content1);
        firstArticle.newComment("userName2", content2);
        entityManager.flush();


        List<String> commentContents = repository.findById(firstArticle.getId())
                .get()
                .getComments()
                .stream()
                .map(ArticleComment::getContent)
                .toList();

        assertThat(commentContents.size()).isEqualTo(2);
        assertThat(commentContents).contains(content1, content2);
    }
}
