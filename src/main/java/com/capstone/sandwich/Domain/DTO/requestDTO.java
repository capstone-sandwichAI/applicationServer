package com.capstone.sandwich.Domain.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class requestDTO {

    private String carNumber;
    private MultipartFile video;


}
