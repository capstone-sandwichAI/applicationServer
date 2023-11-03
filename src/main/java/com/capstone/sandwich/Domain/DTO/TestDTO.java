package com.capstone.sandwich.Domain.DTO;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class TestDTO {
    private List<MultipartFile> photos;
    private MultipartFile video;

}
