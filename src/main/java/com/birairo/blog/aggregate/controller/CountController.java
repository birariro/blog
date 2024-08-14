package com.birairo.blog.aggregate.controller;

import com.birairo.blog.aggregate.service.ArticleLoadCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CountController {
    private final ArticleLoadCount articleLoadCount;

    @GetMapping("/count/article/{id}")
    ResponseEntity loadCount(@PathVariable("id") UUID id) {
        long count = articleLoadCount.loadArticleCount(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "count", count
        ));
    }
}
