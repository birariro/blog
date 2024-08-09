package com.birairo.blog.stroage.controller;

import com.birairo.blog.stroage.service.StorageAdapter;
import com.birairo.blog.stroage.service.Upload;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class StorageController {
    private final StorageAdapter storageAdapter;

    @Operation(
            summary = "이미지 업로드"
    )
    @PostMapping(value = "/storage/upload", consumes = "multipart/form-data")
    public ResponseEntity upload(@RequestParam MultipartFile file) {

        Upload upload = storageAdapter.upload(file);
        Map<String, String> url = Map.of(
                "url", upload.url()
        );
        return ResponseEntity.ok().body(url);
    }


}
