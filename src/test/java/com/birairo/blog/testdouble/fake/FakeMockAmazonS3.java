package com.birairo.blog.testdouble.fake;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;

public class FakeMockAmazonS3 {

    private S3Mock s3Mock;

    private S3Mock getS3Mock() {
        return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
    }

    public AmazonS3 amazonS3() {
        this.s3Mock = getS3Mock();

        String region = "ap-northeast-2";
        s3Mock.start();
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", region);
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();

        return client;
    }

    public void clear() {
        this.s3Mock.shutdown();
    }
}