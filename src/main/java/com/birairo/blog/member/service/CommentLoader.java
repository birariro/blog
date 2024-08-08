package com.birairo.blog.member.service;

import com.birairo.blog.member.domain.Comment;

import java.util.UUID;

public interface CommentLoader {
    ArticleComments findParentComments(UUID parentId);
    Comment findComment(UUID id);
}
