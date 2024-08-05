package com.birairo.blog.article.domain;

import com.birairo.blog.common.UpdatableDomain;
import com.birairo.blog.vo.Content;
import com.birairo.blog.vo.Title;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@ToString(exclude = {"tags"})
public class Article extends UpdatableDomain {

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    public Article(Title title, Content content, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    public void modify(Title title, Content content, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
