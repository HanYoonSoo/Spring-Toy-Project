package com.hanyoonsoo.springtoy.global;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.hanyoonsoo.springtoy.module.global.config.AwsS3Config;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
public class MockAwsS3Config extends AwsS3Config {

    @Bean
    @Primary
    @Override
    public AmazonS3Client amazonS3Client(){
        return Mockito.mock(AmazonS3Client.class);
    }
}
