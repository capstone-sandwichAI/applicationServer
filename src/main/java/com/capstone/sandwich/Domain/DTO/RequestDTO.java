package com.capstone.sandwich.Domain.DTO;


import lombok.*;
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

}
