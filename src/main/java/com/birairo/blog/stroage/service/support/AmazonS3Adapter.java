package com.birairo.blog.stroage.service.support;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.birairo.blog.common.IdGenerator;
import com.birairo.blog.stroage.service.Download;
import com.birairo.blog.stroage.service.StorageAdapter;
import com.birairo.blog.stroage.service.Upload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

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
    public Upload upload(MultipartFile file) {

        Assert.notNull(file, "can not be null");
        Assert.notNull(file.getOriginalFilename(), "can not be null");

        String fileName = uniqueFileName(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String pathWithName = String.format("%s%s", this.path, fileName);
        try {
            amazonS3Client.putObject(new PutObjectRequest(
                    bucket,
                    pathWithName,
                    file.getInputStream(),
                    metadata

            ));
            String preSignedUrl = getPreSignedUrl(bucket, pathWithName);

            return new Upload(fileName, preSignedUrl);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Download download(Upload key) {

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

    /**
     * presigned url 발급
     *
     * @param bucket   버킷  이름
     * @param fileName
     * @return presigned url
     */
    public String getPreSignedUrl(String bucket, String fileName) {


        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, fileName);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    /**
     * 파일 조회용 presigned url 생성
     *
     * @param bucket   버킷 이름
     * @param fileName S3 업로드용 파일 이름
     * @return presigned url
     */
    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.GET);
//                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString()
        );
        return generatePresignedUrlRequest;
    }

    /**
     * presigned url 유효 기간 설정
     *
     * @return 유효기간
     */
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

}


