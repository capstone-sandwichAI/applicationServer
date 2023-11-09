package com.capstone.sandwich.Domain.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BackResponseDTO {

    private String carNumber;
    private List<String> imageUrlList;

    private Integer scratch; //스크래치 개수
    private Integer installation; // 장착 불량 개수
    private Integer exterior; //외관 손상 개수
    private Integer gap; // 단차 손상 개수
    private Integer totalDefects;

    //자동 레포트
    private String report;
}
