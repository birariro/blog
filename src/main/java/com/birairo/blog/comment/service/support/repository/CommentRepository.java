package com.birairo.blog.comment.service.support.repository;

import com.birairo.blog.comment.domain.Comment;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends Repository<Comment, UUID> {
    Comment save(Comment comment);

    Optional<Comment> findById(UUID id);

    List<Comment> findByParentId(UUID parentId);

    boolean existsById(UUID id);
}
