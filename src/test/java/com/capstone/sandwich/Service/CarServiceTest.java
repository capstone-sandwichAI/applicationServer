package com.capstone.sandwich.Service;

import com.capstone.sandwich.Domain.DTO.RequestDTO;
import com.capstone.sandwich.Domain.Exception.ApiException;
import com.capstone.sandwich.Repository.CarImagesRepository;
import com.capstone.sandwich.Repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(classes = CarServiceTest.TestConfig.class)
@RequiredArgsConstructor
class CarServiceTest {

    @Configuration
    static class TestConfig {

        @Bean
        public CarService carService() {
            return new CarService();
        }
    }

    @Autowired
    private CarService carService;

    @Test
    void Validation() throws IOException, ApiException {

        File file1 = new File("src/test/resources/testPhotos/roofSide.png");
        MultipartFile multipartFile1 = new MockMultipartFile(file1.getName(), file1.getName(), "image/png", Files.readAllBytes(file1.toPath()));
        List<MultipartFile> files = new ArrayList<>();
        files.add(multipartFile1);
        RequestDTO dto = new RequestDTO("A", files);
        Integer size = carService.validateDTO(dto);
        Assertions.assertThat(size).isEqualTo(0);
    }

}