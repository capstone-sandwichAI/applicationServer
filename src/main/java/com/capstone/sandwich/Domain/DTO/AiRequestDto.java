package com.capstone.sandwich.Domain.DTO;

import lombok.*;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiRequestDto {

    private String carNumber;
    private List<FileSystemResource> images;
}
