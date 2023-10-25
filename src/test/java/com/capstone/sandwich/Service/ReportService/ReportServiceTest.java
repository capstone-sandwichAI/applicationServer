package com.capstone.sandwich.Service.ReportService;

import com.capstone.sandwich.Domain.Entity.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


@SpringJUnitConfig(classes = ReportServiceTest.TestConfig.class)
class ReportServiceTest {

    @Configuration
    static class TestConfig {
        @Bean
        public ReportService reportService() {
            return new ReportService();
        }
    }

    @Autowired
    private ReportService reportService;
    private String request;

    @BeforeEach
    void makeRequest() throws JsonProcessingException {
        request=reportService.makeRequest(new Car());


    }


    @Test
    void getResponse() {
        Car car = new Car();

//        request = "what is chatgpt?";
        String response = reportService.getResponse(request, 1.0f, 500);

    }


}
