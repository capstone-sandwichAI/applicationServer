package com.capstone.sandwich.Service.ReportService;

import com.capstone.sandwich.Domain.Entity.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


@SpringJUnitConfig(classes = reportServiceTest.TestConfig.class)
class reportServiceTest {

    @Configuration
    static class TestConfig {
        @Bean
        public reportService reportService() {
            return new reportService();
        }
    }

    @Autowired
    private reportService reportService;

    @Test
    void makeRequest() throws JsonProcessingException {
        Car car = new Car();

        String request = reportService.makeRequest(car);
//        System.out.println("request = " + request);
    }


}
