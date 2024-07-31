package com.birairo.blog.common;


public class NoSuchEntityException extends RuntimeException {
    /**
     * HTTP Response Header 의 status 가 404 NOT_FOUND 로 전달 된다
     * @param message
     */
    public NoSuchEntityException(String message) {
        super(message);
    }
}
