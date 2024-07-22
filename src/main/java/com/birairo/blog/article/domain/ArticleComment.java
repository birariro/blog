package com.birairo.blog.article.domain;

import com.birairo.common.Domain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"article"})
public class ArticleComment extends Domain {
    @ManyToOne
    private Article article;
    @Column(length = 100,nullable = false)
    private String author;
    @Column(length = 5000,nullable = false)
    private String content;

    public ArticleComment(String author, String content) {
        this.author = author;
        this.content = content;
        //todo 왜  article 를 연결하지 않아도 저장이되는거지?
    }
}
