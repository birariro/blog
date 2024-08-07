package com.birairo.blog.comment.service.support;

import com.birairo.blog.comment.domain.Comment;
import com.birairo.blog.vo.Parent;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface CommentRepository extends Repository<Comment, UUID> {
    Comment save(Comment comment);

    Optional<Comment> findById(UUID id);

    List<Comment> findByParentId(Parent parentId);

    boolean existsById(UUID id);
}
