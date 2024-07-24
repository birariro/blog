package com.birairo.blog.article.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 100,nullable = false)
    private String title;
    @Column(length = 5000,nullable = false)
    private String content;
    @OneToMany(mappedBy = "article")
    private List<ArticleComment> comments = new ArrayList<>();

    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
