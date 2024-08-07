package com.birairo.blog.comment.domain;

import com.birairo.blog.common.UpdatableDomain;
import com.birairo.blog.vo.Author;
import com.birairo.blog.vo.Content;
import com.birairo.blog.vo.Parent;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Embedded
    private Parent parentId;

    @Column(name = "parent_type")
    @Enumerated(EnumType.STRING)
    private ParentType parentType;
    @Embedded
    private Author author;
    @Embedded
    private Content content;

    private Comment(Parent parentId, ParentType type, Author author, Content content) {
        this.parentId = parentId;
        this.parentType = type;
        this.author = author;
        this.content = content;
    }

    public static Comment ofArticleComment(UUID articleId, Author author, Content content) {
        return new Comment(Parent.of(articleId), ParentType.ARTICLE, author, content);
    }

    public static Comment ofCommentToComment(UUID commentId, Author author, Content content) {
        return new Comment(Parent.of(commentId), ParentType.COMMENT, author, content);
    }

    public void modify(Author author, Content content) {
        this.author = author;
        this.content = content;
    }
}
