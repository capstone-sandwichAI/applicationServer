package com.capstone.sandwich.Service;

import com.capstone.sandwich.Domain.DTO.AiResponseDTO;
import com.capstone.sandwich.Domain.DTO.RequestDTO;
import com.capstone.sandwich.Domain.Entity.CustomMultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AiRequestWebClientTest {


    private static String ENDPOINT = "http://172.20.10.7:5000";

    public WebClient buildWebClient() {

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
                .build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(ENDPOINT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .build();
    }
    @Test
    void aiRequest() throws IOException {

        File file1 = new File("src/test/resources/testPhotos/IMG1.jpg");
        File file2 = new File("src/test/resources/testPhotos/IMG2.jpg");

        MultipartFile multipartFile1 = new MockMultipartFile(file1.getName(), file1.getName(), "image/png", Files.readAllBytes(file1.toPath()));
        MultipartFile multipartFile2 = new CustomMultipartFile(Files.readAllBytes(file2.toPath()), file2.getName(), file2.getName(), "image/png", Files.readAllBytes(file2.toPath()).length);
        List<MultipartFile> images = new ArrayList<>();
        images.add(multipartFile1);
        images.add(multipartFile2);

        String testCarNum = "TestCarNum";
        RequestDTO requestDTO = new RequestDTO(testCarNum, images);

        AiResponseDTO response = getRespose(requestDTO);
        System.out.println("scratch = " + response.getScratch());
        response.setCarNumber(testCarNum);

        List<String> encodedImages = response.getEncodedImages();
//        System.out.println("encodedImages = " + encodedImages);
        response.setImageList(encodedImages);
        System.out.println("response = " + response.getImageList());

        saveImages(response);
    }

    private static void saveImages(AiResponseDTO responseDTO) {
        responseDTO.getImageList().stream().forEach(
                file ->{
                    String fileName = file.getOriginalFilename();
                    String filePath = "src/test/resources/testPhotos/" + fileName;

                    // 파일 저장
                    try {
                        file.transferTo(new File(filePath));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private AiResponseDTO getRespose(RequestDTO requestDTO) throws JsonProcessingException {

        MultiValueMap<String, Object> requestBody = requestDTO.makeRequestForm();

        AiResponseDTO responseDTO = buildWebClient().post()
                .uri("/v2/object-detection/best")
                .body(BodyInserters.fromMultipartData(requestBody))
                .retrieve()
                .bodyToMono(AiResponseDTO.class)
                .block();
        return responseDTO;
    }

    private static File multipartFileToFile(MultipartFile mfile) throws IOException {
        File file = new File(mfile.getOriginalFilename());
        mfile.transferTo(file);
        return file;
    }



}
