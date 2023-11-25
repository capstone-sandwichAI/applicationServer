package com.capstone.sandwich.Domain.DTO;


import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {

    private String carNumber;
    private List<MultipartFile> imageList;

    public MultiValueMap<String, Object>makeRequestForm(){

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

        imageList.stream()
                .forEach(file -> formData.add("images", file.getResource()));

        return formData;
    }
}
