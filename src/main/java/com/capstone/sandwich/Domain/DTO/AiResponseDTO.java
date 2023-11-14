package com.capstone.sandwich.Domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AiResponseDTO {
    private String carNumber;
    private List<MultipartFile> imageList;

    private Integer scratch; //스크래치 개수
    private Integer installation; // 장착 불량 개수
    private Integer exterior; //외관 손상 개수
    private Integer gap; // 단차 손상 개수

    private Integer totalDefects;
}
