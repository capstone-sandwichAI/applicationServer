package com.capstone.sandwich.Service.ReportService;

import com.capstone.sandwich.Domain.Entity.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class reportService {

    ObjectMapper objectMapper = new ObjectMapper();

    public String makeRequest(Car car) throws JsonProcessingException {
        String request="";
        request += car.getClass().toString();
        String carJson = objectMapper.writeValueAsString(car);
        log.info("request = {}", carJson);
        return carJson;
    }
}
