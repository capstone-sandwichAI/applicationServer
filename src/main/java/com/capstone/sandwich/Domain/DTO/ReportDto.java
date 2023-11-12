package com.capstone.sandwich.Domain.DTO;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private String carNumber;
    private Integer scratch;
    private Integer installation;
    private Integer exterior;
    private Integer gap;
    private Integer totalDefects;

    private LocalDate createdDate;
}
