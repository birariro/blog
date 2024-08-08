package com.birairo.blog.member.service.support;

import com.birairo.blog.member.domain.Comment;
import com.birairo.blog.vo.Parent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface CommentRepository extends Repository<Comment, UUID> {
    Comment save(Comment comment);

    Optional<Comment> findById(UUID id);

    @Query("select c from Comment c left join fetch c.author where c.parentId = :parentId")
    List<Comment> findByParentIdWithAuthor(Parent parentId);

    boolean existsById(UUID id);
}
