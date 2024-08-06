package com.birairo.blog.stroage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.birairo.blog.common.IdGenerator;
import com.birairo.blog.stroage.service.support.AmazonS3Adapter;
import com.birairo.blog.testdouble.fake.FakeMockAmazonS3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class StorageAdapterTest {
    private AmazonS3Adapter sut;
    private IdGenerator idGenerator = new IdGenerator() {
        @Override
        public String generateId() {
            return "id";
        }
    };
    private MockMultipartFile uploadFile;
    private FakeMockAmazonS3 fakeMockAmazonS3;

    @BeforeEach
    void init() throws IOException {

        this.uploadFile = new MockMultipartFile(
                "file",
                "imageName.png",
                "image/png",
                new ByteArrayInputStream(getImage())
        );


        this.fakeMockAmazonS3 = new FakeMockAmazonS3();
        AmazonS3 amazonS3 = fakeMockAmazonS3.amazonS3();
        String bucketName = "bucketname";
        amazonS3.createBucket(bucketName);

        this.sut = new AmazonS3Adapter(amazonS3, idGenerator, bucketName, "dirpath");
    }

    @AfterEach
    void clear() {
        this.fakeMockAmazonS3.clear();
    }

    @Test
    void should_upload() {

        FileKey fileKey = sut.upload(uploadFile);

        assertThat(fileKey).isNotNull();
        assertThat(fileKey.value())
                .isNotBlank()
                .contains(idGenerator.generateId());
    }

    @Test
    void should_upload_to_download() throws IOException {

        FileKey fileKey = sut.upload(uploadFile);
        Download DownloadFile = sut.download(fileKey);

        assertThat(DownloadFile.name()).isEqualTo(fileKey.value());
        assertThat(DownloadFile.contentType()).isEqualTo(uploadFile.getContentType());
        assertThat(new String(DownloadFile.resource())).isEqualTo(new String(uploadFile.getBytes()));
    }

    @Test
    void should_Download_then_ResponseEntity() throws IOException {

        Download download = new Download(uploadFile.getName(), uploadFile.getBytes(), uploadFile.getContentType());

        ResponseEntity<byte[]> response = ResponseEntity.ok()
                .contentType(MediaType.valueOf(download.contentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(download.name(), StandardCharsets.UTF_8).build().toString()
                )
                .body(download.resource());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(download.resource());
    }

    private byte[] getImage() throws IOException {
        int width = 10;
        int height = 10;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }
}