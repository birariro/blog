package com.birairo.blog.comment.facade;

import com.birairo.blog.vo.Author;
import com.birairo.blog.vo.Content;
import com.birairo.blog.comment.domain.Comment;
import com.birairo.blog.comment.repository.CommentRepository;
import com.birairo.blog.common.NoSuchEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public void saveComment(UUID target, String author, String content) {

        Author _author = Author.of(author);
        Content _content = Content.of(content);
        Comment comment = new Comment(target, _author, _content);
        commentRepository.save(comment);
    }

    @Transactional
    public void modifyComment(UUID id, String author, String content) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(""));
    }

    public List<Comment> findTargetComments(UUID target) {
        return commentRepository.findByTarget(target);
    }
}
