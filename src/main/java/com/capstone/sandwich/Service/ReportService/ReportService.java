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
    private static final String ENDPOINT = "https://api.openai.com/v1/completions";
    ObjectMapper objectMapper = new ObjectMapper();

    public String makeRequest(Car car) throws JsonProcessingException {
        String request="";
        request += car.getClass().toString();
        String carJson = objectMapper.writeValueAsString(car);

        log.info("request = {}", carJson);
        return carJson;
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
        requestBody.put("model","text-davinci-003");
        requestBody.put("prompt", prompt);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);
        return requestBody;
    }


}
