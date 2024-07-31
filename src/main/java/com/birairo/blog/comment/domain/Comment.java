package com.birairo.blog.comment.domain;

import com.birairo.blog.vo.Author;
import com.birairo.blog.vo.Content;
import com.birairo.blog.common.UpdatableDomain;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
public class Comment extends UpdatableDomain {

    @Column
    private UUID target;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "author", length = 100, nullable = false))
    private Author author;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "content", length = 5000, nullable = false))
    private Content content;

    public Comment(UUID target, Author author, Content content) {
        this.target = target;
        this.author = author;
        this.content = content;
    }
}
