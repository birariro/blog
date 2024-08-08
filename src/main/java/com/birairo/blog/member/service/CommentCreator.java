package com.birairo.blog.member.service;

import com.birairo.blog.vo.Author;
import com.birairo.blog.vo.Content;

import java.util.UUID;

public interface CommentCreator {
    void createArticleComment(UUID articleId, Author author, Content content);

    void createCommentToComment(UUID commentId, Author author, Content content);
}
