package com.birairo.blog.stroage.service.support;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.birairo.blog.common.IdGenerator;
import com.birairo.blog.stroage.service.Download;
import com.birairo.blog.stroage.service.FileKey;
import com.birairo.blog.stroage.service.StorageAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class AmazonS3Adapter implements StorageAdapter {

    private final AmazonS3 amazonS3Client;
    private final IdGenerator idGenerator;
    private final String bucket;
    private final String path;


    public AmazonS3Adapter(
            AmazonS3 amazonS3Client,
            IdGenerator idGenerator,
            @Value("${aws.s3.bucket}") String bucket,
            @Value("${aws.s3.path}") String path
    ) {

        this.amazonS3Client = amazonS3Client;
        this.idGenerator = idGenerator;
        this.bucket = bucket;

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        this.path = path;
    }

    @Override
    public FileKey upload(MultipartFile file) {

        Assert.notNull(file, "can not be null");
        Assert.notNull(file.getOriginalFilename(), "can not be null");

        String fileName = uniqueFileName(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            amazonS3Client.putObject(new PutObjectRequest(
                    bucket,
                    String.format("%s%s", this.path, fileName),
                    file.getInputStream(),
                    metadata

            ));
            return new FileKey(fileName);
        } catch (IOException e) {
            //todo 예외 정의
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Download download(FileKey key) {

        String downloadPath = String.format("%s%s", this.path, key.value());
        S3Object object = amazonS3Client.getObject(bucket, downloadPath);
        try {

            byte[] resource = IOUtils.toByteArray(object.getObjectContent());
            String contentType = object.getObjectMetadata().getContentType();

            return new Download(key.value(), resource, contentType);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }


    private String uniqueFileName(String name) {

        int index = name.lastIndexOf(".");

        if (index == -1) {
            //todo 확장자가 없다면 예외 인가
            throw new IllegalArgumentException();
        }
        String extension = name.substring(index);
        return idGenerator.generateId() + extension;
    }

}


