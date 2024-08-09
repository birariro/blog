package com.birairo.blog.stroage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageAdapter {
    Upload upload(MultipartFile file);

    Download download(Upload key);
}
