package com.birairo.blog.member.controller;

import com.birairo.blog.member.service.ArticleComments;
import com.birairo.blog.member.service.CommentCreator;
import com.birairo.blog.member.service.CommentLoader;
import com.birairo.blog.member.service.CommentModifier;
import com.birairo.blog.vo.Author;
import com.birairo.blog.vo.Client;
import com.birairo.blog.vo.Content;
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

import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentLoader commentLoader;
    private final CommentCreator commentCreator;
    private final CommentModifier commentModifier;

    @PostMapping("/comment/{id}/comment")
    ResponseEntity<Void> createCommentToComment(
            Client client,
            @PathVariable("id") UUID commentId,
            @RequestBody CreateCommentRequest request
    ) {

        commentCreator.createCommentToComment(
                commentId,
                Author.of(client.getId()),
                Content.of(request.content())
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/article/{id}/comment")
    ResponseEntity<Void> createArticleComment(
            Client client,
            @PathVariable("id") UUID articleId,
            @RequestBody CreateCommentRequest request
    ) {
        commentCreator.createArticleComment(
                articleId,
                Author.of(client.getId()),
                Content.of(request.content())
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/article/{id}/comment")
    ResponseEntity findComment(@PathVariable("id") UUID target) {
        ArticleComments targetComments = commentLoader.findParentComments(target);
        return ResponseEntity.status(HttpStatus.OK).body(targetComments);
    }

    @PutMapping("/article/{id}/comment")
    ResponseEntity<Void> modifyComment(Client client, @PathVariable("id") UUID id, @RequestBody ModifyCommentRequest request) {
        commentModifier.modifyComment(
                id,
                Author.of(client.getId()),
                Content.of(request.content())
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}