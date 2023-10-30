package com.capstone.sandwich.Service;

import com.capstone.sandwich.Domain.DTO.RequestDTO;
import com.capstone.sandwich.Domain.Exception.ApiException;
import com.capstone.sandwich.Repository.CarImagesRepository;
import com.capstone.sandwich.Repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@RequiredArgsConstructor
@Transactional
class CarServiceTest {

    @Autowired
    private CarService carService;

    @Test
    void ValidationSuccess() throws IOException, ApiException {

        File file1 = new File("src/test/resources/testPhotos/roofSide.png");
        MultipartFile multipartFile1 = new MockMultipartFile(file1.getName(), file1.getName(), "image/png", Files.readAllBytes(file1.toPath()));
        List<MultipartFile> files = new ArrayList<>();
        files.add(multipartFile1);
        RequestDTO dto = new RequestDTO("A", files);
        Integer size = carService.validateDTO(dto);
        assertThat(size).isEqualTo(0);
    }
    @Test
    void ValidationNull(){
        List<MultipartFile> files = new ArrayList<>();
        RequestDTO dto = new RequestDTO("A", files);
        assertThrows(ApiException.class, () -> carService.validateDTO(dto));
    }

    @Test
    void ValidationUnsupportedMedia() throws ApiException, IOException {
        File file1 = new File("src/test/resources/testPhotos/roofSide.png");
        File file2 = new File("src/test/resources/testPhotos/bumper.jpg");
        MultipartFile multipartFile1 = new MockMultipartFile(file1.getName(), file1.getName(), "image/png", Files.readAllBytes(file1.toPath()));
        MultipartFile multipartFile2 = new MockMultipartFile(file1.getName(), file2.getName(), "image/jpeg", Files.readAllBytes(file2.toPath()));

        List<MultipartFile> files = new ArrayList<>();
        files.add(multipartFile1);
        files.add(multipartFile2);
        RequestDTO dto = new RequestDTO("A", files);
        assertThrows(ApiException.class, () -> carService.validateDTO(dto));
        
    }

}