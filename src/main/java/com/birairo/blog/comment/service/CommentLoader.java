package com.birairo.blog.comment.service;

import com.birairo.blog.comment.domain.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentLoader {
    ArticleComments findParentComments(UUID parentId);
    Comment findComment(UUID id);
}
