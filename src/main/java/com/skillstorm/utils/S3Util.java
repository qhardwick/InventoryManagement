package com.skillstorm.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class S3Util {

    @Bean
    public S3AsyncClient s3() {
        return S3AsyncClient.builder().region(Region.US_EAST_2).build();
    }
}
