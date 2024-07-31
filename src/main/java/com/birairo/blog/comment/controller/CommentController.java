package com.birairo.blog.comment.controller;

import com.birairo.blog.comment.domain.Comment;
import com.birairo.blog.comment.facade.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    ResponseEntity<Void> createArticleComment(@RequestBody CreateCommentRequest request) {

        commentService.saveComment(
                request.target(),
                request.author(),
                request.content()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/comment/target/{id}")
    ResponseEntity findComment(@PathVariable("id") UUID target) {
        List<Comment> targetComments = commentService.findTargetComments(target);
        List<CommentResponse> content = targetComments.stream().map(CommentResponse::new).toList();
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    @PutMapping("/comment/{id}")
    ResponseEntity<Void> modifyComment(@PathVariable("id") UUID id, @RequestBody ModifyCommentRequest request) {
        commentService.saveComment(
                id,
                request.author(),
                request.content()
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
