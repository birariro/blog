package com.birairo.blog.comment.service;

import com.birairo.blog.vo.Author;
import com.birairo.blog.vo.Content;

import java.util.UUID;

public interface CommentModifier {
    void modifyComment(UUID id, Author author, Content content);
}
