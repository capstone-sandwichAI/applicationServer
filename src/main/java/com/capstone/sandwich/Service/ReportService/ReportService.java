package com.capstone.sandwich.Service.ReportService;

import com.capstone.sandwich.Domain.Entity.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ReportService {

    @Value("${apikey}")
    private String API_KEY;
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";
    ObjectMapper objectMapper = new ObjectMapper();

    public String makeRequest(Car car) throws JsonProcessingException {
        String request= objectMapper.writeValueAsString(car);

//        request= carJson.replace("scratch", "스크래치")
//                .replace("installation", "장착 불량")
//                .replace("exterior", "외관 손상")
//                .replace("gap", "단차")
//                .replace("totalDefects","전체 불량 개수");

//        request += " 다음은 외관 불량 테스트 결과이다. 이를 바탕으로 레포트를 작성해줘라";
        request += "This is our Car Inspection result. Make a report by this result.";
        log.info("request = {}", request);
        return request;
    }

    public String getResponse(String prompt, float temperature, int maxTokens) {
        HttpHeaders headers = makeRequestHeader();
        Map<String, Object> requestBody = makeRequestBody(prompt, temperature, maxTokens);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = getApiResponse(requestEntity);
        log.info("get response: {}", response);

        return response.toString();
    }

    private static ResponseEntity<Map> getApiResponse(HttpEntity<Map<String, Object>> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, requestEntity, Map.class);
        return response;
    }

    private HttpHeaders makeRequestHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);
        return headers;
    }
    private static Map<String, Object> makeRequestBody(String prompt, float temperature, int maxTokens) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model","gpt-3.5-turbo");

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        requestBody.put("messages", new Object[]{message});;

        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);
        return requestBody;
    }


}
