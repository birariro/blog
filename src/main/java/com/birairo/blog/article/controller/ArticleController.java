package com.birairo.blog.article.controller;

import com.birairo.blog.article.domain.Article;
import com.birairo.blog.article.domain.Tag;
import com.birairo.blog.article.service.ArticleCreator;
import com.birairo.blog.article.service.ArticleHeader;
import com.birairo.blog.article.service.ArticleLoader;
import com.birairo.blog.article.service.ArticleModifier;
import com.birairo.blog.vo.Content;
import com.birairo.blog.vo.Title;
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
public class ArticleController {

    private final ArticleCreator articleCreator;
    private final ArticleModifier articleModifier;
    private final ArticleLoader articleLoader;

    @PostMapping("/article")
    ResponseEntity<Void> createArticle(@RequestBody ArticleRequest request) {

        articleCreator.createArticle(
                Title.of(request.title()),
                Content.of(request.content()),
                Tag.of(request.tags())
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/article/{id}")
    ResponseEntity<Void> modifyArticle(
            @PathVariable("id") UUID id,
            @RequestBody ArticleRequest request
    ) {

        articleModifier.modifyArticle(
                id,
                Title.of(request.title()),
                Content.of(request.content()),
                Tag.of(request.tags())
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/article")
    ResponseEntity loadArticle() {
        List<ArticleHeader> content = articleLoader.findArticleHeader();
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    @GetMapping("/article/{id}")
    ResponseEntity loadArticle(@PathVariable("id") UUID id) {
        Article content = articleLoader.findArticle(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ArticleResponse(content));
    }
}
