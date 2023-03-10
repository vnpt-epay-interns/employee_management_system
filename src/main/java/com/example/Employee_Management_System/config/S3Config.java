package com.example.Employee_Management_System.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${amazon.s3.region}")
    private String region;

    @Value("${amazon.s3.default-bucket}")
    private String bucketName;

    @Value("${amazon.aws.access-key-id}")
    private String accessKey;

    @Value("${amazon.aws.access-key-secret}")
    private String secretKey;

    @Bean
    public AmazonS3 amazonS3() {

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))).build();
    }
}
