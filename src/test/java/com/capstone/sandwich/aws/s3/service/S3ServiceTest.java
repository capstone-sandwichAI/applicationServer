package com.capstone.sandwich.aws.s3.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

@SpringBootTest
public class S3ServiceTest {

    @Autowired
    private S3Service s3Service;

    @Test
    public void S3_버킷에_이미지_업로드() throws IOException {

        MockMultipartFile file = new MockMultipartFile("image", "image.jpg", "image/jpg", "image content".getBytes());

        String imageUrl = s3Service.upload(file);

        assert imageUrl != null && !imageUrl.isEmpty();
    }
}
