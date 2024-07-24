package com.birairo.blog.article.domain;

import com.birairo.common.Domain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    @Column(length = 100,nullable = false)
    private String author;
    @Column(length = 5000,nullable = false)
    private String content;

    public ArticleComment setArticle(Article article) {
        this.article = article;
        return this;
    }

    public ArticleComment setContent(String content) {
        this.content = content;
        return this;
    }

    public ArticleComment(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public ArticleComment(Article article, String author, String content) {
        this.article = article;
        this.author = author;
        this.content = content;
    }
}
