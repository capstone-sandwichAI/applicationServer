package com.capstone.sandwich.Domain.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class RequestDTO {

    private String carNumber;
    private List<MultipartFile> photos;

}
