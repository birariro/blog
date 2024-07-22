package com.birairo.blog.config;

import com.birairo.blog.common.NoSuchEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHttpStatusHandler {
    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity notFoundHandler(NoSuchEntityException exception) {

        log.info(String.format("NoSuchEntityException Message : ` %s `", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}