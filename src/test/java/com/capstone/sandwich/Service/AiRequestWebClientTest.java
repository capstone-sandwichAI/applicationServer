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

    //따로 받은 아마존 IP 주소를 사용
    private static String ENDPOINT = "http://172.20.10.7:5000";

    //Web client 디폴트로 빌드하는 부분
    public WebClient buildWebClient() {

        //사진 크기 상관없게 해주는 역할
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
                .build();


        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(ENDPOINT) //엔드포인트
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)//멀티 파트로 설정
                .build();
    }
    @Test
    void aiRequest() throws IOException {

        //여기서부터는 Multipart 리스트 만드는 부분
        File file1 = new File("src/test/resources/testPhotos/IMG1.jpg");
        File file2 = new File("src/test/resources/testPhotos/IMG2.jpg");

        MultipartFile multipartFile1 = new MockMultipartFile(file1.getName(), file1.getName(), "image/png", Files.readAllBytes(file1.toPath()));
        MultipartFile multipartFile2 = new CustomMultipartFile(Files.readAllBytes(file2.toPath()), file2.getName(), file2.getName(), "image/png", Files.readAllBytes(file2.toPath()).length);

        //이게 MultipartList
        List<MultipartFile> images = new ArrayList<>();
        images.add(multipartFile1);
        images.add(multipartFile2);

        //테스트 car number
        String testCarNum = "TestCarNum";

        RequestDTO requestDTO = new RequestDTO(testCarNum, images);

        //Ai로부터 response 받아옴
        AiResponseDTO response = getRespose(requestDTO);
        response.setImageList();

        System.out.println("scratch = " + response.getScratch());
        response.setCarNumber(testCarNum);

        List<String> encodedImages = response.getEncodedImages();

        System.out.println("response = " + response.getImageList());

//        saveImages(response);
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

    //주 로직
    private AiResponseDTO getRespose(RequestDTO requestDTO) throws JsonProcessingException {

        //이 자료형만 멀티파트로 하는데 가능하더라
        MultiValueMap<String, Object> requestBody = requestDTO.makeRequestForm();

        //request post문 작성
        AiResponseDTO responseDTO = buildWebClient().post()
                .uri("/v2/object-detection/best") //추가 url 넣어주고
                .body(BodyInserters.fromMultipartData(requestBody))//바디 넣는 부분
                .retrieve()
                .bodyToMono(AiResponseDTO.class)
                .block();
        //TODO request에 대해서 실패한 경우 AiResponseDTO로 안 오는듯?
        return responseDTO;
    }

    private static File multipartFileToFile(MultipartFile mfile) throws IOException {
        File file = new File(mfile.getOriginalFilename());
        mfile.transferTo(file);
        return file;
    }



}
