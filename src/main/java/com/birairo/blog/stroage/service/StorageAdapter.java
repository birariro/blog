package com.birairo.blog.stroage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageAdapter {
    FileKey upload(MultipartFile file);

    Download download(FileKey key);
}
