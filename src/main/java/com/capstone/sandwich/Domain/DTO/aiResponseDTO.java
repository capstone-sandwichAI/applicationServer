package com.capstone.sandwich.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class aiResponseDTO {
    private String carNumber;
    private MultipartFile resultVideo;

    private Integer scratch=0; //스크래치 개수
    private Integer installation=0; // 장착 불량 개수
    private Integer exterior=0; //외관 손상 개수
    private Integer gap=0; // 단차 손상 개수

    private Integer totalDefects=0;
}
