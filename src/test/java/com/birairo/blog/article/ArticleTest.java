package com.birairo.blog.article;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.domain.ArticleComment;
import com.birairo.blog.article.repository.ArticleRepository;
import com.birairo.blog.env.DomainTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@Rollback(value = false)
@DomainTest
class ArticleTest {

    @Autowired
    ArticleRepository repository;

    @Autowired
    EntityManager entityManager;

    private Article firstArticle;

    @BeforeEach
    void init(){


//        this.firstArticle = repository.save(this.firstArticle);
//
//        assertThat(firstArticle)
//                .isEqualTo(repository.findById(firstArticle.getId()).get());
    }

    @Test
    void success(){

        Article article = new Article("title1", "content1");

        ArticleComment articleComment = new ArticleComment("userName1", "good");
        article.getComments().add(articleComment);
        articleComment.setArticle(article);

        int commentCount = article.getComments().size();
        assertThat(commentCount).isEqualTo(1);
    }

    @Test
    void bug(){

        Article article = new Article("title1", "content1");

        ArticleComment articleComment = new ArticleComment("userName1", "good");
        articleComment.setContent("new content");
        articleComment.setArticle(article);
        article.getComments().add(articleComment);

        int commentCount = article.getComments().size();
        assertThat(commentCount).isEqualTo(2);
    }


    @Test
    void bug2(){

        Article article = new Article("title1", "content1");

        ArticleComment articleComment = new ArticleComment(article,"userName1", "good");
        article.getComments().add(articleComment);

        int commentCount = article.getComments().size();
        assertThat(commentCount).isEqualTo(2);
    }
}
