package com.birairo.blog.stroage.service;

public record Download(
        String name,
        byte[] resource,
        String contentType
) {
}
