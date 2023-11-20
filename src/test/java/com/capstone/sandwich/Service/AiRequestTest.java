package com.capstone.sandwich.Service;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AiRequestTest {

    private static String ENDPOINT = "http://ec2-3-137-69-122.us-east-2.compute.amazonaws.com:5000/v1/object-detection";

    private static ResponseEntity<String> getApiResponse() throws IOException {
        MultiValueMap<String, Object> requestBody = makeRequestBody();

        HttpHeaders headers = makeRequestHeader();

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINT, requestEntity, String.class);
        return response;
    }

    private static HttpHeaders makeRequestHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }
    private static MultiValueMap<String, Object> makeRequestBody() throws IOException {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        // 이미지 파일을 바이트 배열로 읽어와서 request body에 추가
        Path imagePath = Paths.get("src/test/resources/testPhotos/bumper.jpg");  // 이미지 파일 경로를 실제 파일 경로로 변경
        byte[] imageData = Files.readAllBytes(imagePath);

        // 이미지 파일을 MultiValueMap에 추가
        requestBody.add("image", new FileSystemResource(new File(imagePath.toString())));

        return requestBody;
    }
    @Test
    void requestToAi() throws IOException {
        ResponseEntity<String> apiResponse = getApiResponse();
        System.out.println("apiResponse = " + apiResponse);
    }
}
