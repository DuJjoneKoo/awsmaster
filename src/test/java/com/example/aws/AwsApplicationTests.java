package com.example.aws;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@MockBean(S3Client.class)
class AwsApplicationTests {

    @Test
    void contextLoads() {
    }

}
