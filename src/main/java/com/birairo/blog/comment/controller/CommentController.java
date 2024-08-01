package com.birairo.blog.comment.controller;

import com.birairo.blog.comment.domain.Comment;
import com.birairo.blog.comment.facade.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/article/{id}/comment")
    ResponseEntity<Void> createArticleComment(@PathVariable("id") UUID articleId, @RequestBody CreateCommentRequest request) {

        commentService.saveArticleComment(
                articleId,
                request.author(),
                request.content()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/article/{id}/comment")
    ResponseEntity findComment(@PathVariable("id") UUID target) {
        List<Comment> targetComments = commentService.findParentComments(target);
        List<CommentResponse> content = targetComments.stream().map(CommentResponse::new).toList();
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    @PutMapping("/article/{id}/comment")
    ResponseEntity<Void> modifyComment(@PathVariable("id") UUID id, @RequestBody ModifyCommentRequest request) {
        commentService.saveArticleComment(
                id,
                request.author(),
                request.content()
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
