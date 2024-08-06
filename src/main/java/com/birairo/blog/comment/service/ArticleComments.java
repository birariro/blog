package com.birairo.blog.comment.service;

import com.birairo.blog.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
public class ArticleComments {

    private final UUID articleId;
    private List<ArticleComment> comments;

    public ArticleComments(UUID articleId, List<Comment> comments) {
        this.articleId = articleId;
        this.comments = comments.stream()
                .map(ArticleComment::new)
                .sorted(Comparator.comparing(ArticleComment::getCreateAt))
                .toList();
    }

    public void setNestedComments(UUID id, List<Comment> comments) {
        ArticleComment articleComment = this.comments.stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst()
                .orElseThrow();

        articleComment.setCommentToComment(comments);
    }

    @Getter
    public class ArticleComment {

        private final UUID id;
        private final String author;
        private final String content;
        private final LocalDateTime createAt;
        private List<NestedComment> comments;

        public ArticleComment(Comment comment) {
            this.id = comment.getId();
            this.author = comment.getAuthor().getValue();
            this.content = comment.getContent().getValue();
            this.createAt = comment.getCreatedAt();
            this.comments = new ArrayList<>();
        }

        public void setCommentToComment(List<Comment> comments) {
            this.comments = comments.stream()
                    .map(NestedComment::new)
                    .sorted(Comparator.comparing(NestedComment::getCreateAt))
                    .toList();


        }
    }

    @Getter
    private class NestedComment {
        private final UUID id;
        private final String author;
        private final String content;
        private final LocalDateTime createAt;

        public NestedComment(Comment comment) {
            this.id = comment.getId();
            this.author = comment.getAuthor().getValue();
            this.content = comment.getContent().getValue();
            this.createAt = comment.getCreatedAt();
        }
    }

}

