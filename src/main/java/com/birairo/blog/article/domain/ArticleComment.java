package com.birairo.blog.article.domain;

import com.birairo.blog.article.vo.Author;
import com.birairo.blog.article.vo.Content;
import com.birairo.blog.common.UpdatableDomain;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
public class ArticleComment extends UpdatableDomain {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "author", length = 100, nullable = false))
    private Author author;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "content", length = 5000, nullable = false))
    private Content content;

    public ArticleComment(Article article, Author author, Content content) {
        this.article = article;
        this.author = author;
        this.content = content;
    }
}
