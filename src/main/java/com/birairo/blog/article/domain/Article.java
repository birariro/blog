package com.birairo.blog.article.domain;

import com.birairo.common.Domain;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"comments"})
public class Article extends Domain {

    @Column(length = 100,nullable = false)
    private String title;
    @Column(length = 5000,nullable = false)
    private String content;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleComment> comments = new ArrayList<>();

    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void newComment(String author, String content) {
        ArticleComment articleComment = new ArticleComment(author, content);
        this.comments.add(articleComment);
    }
}
