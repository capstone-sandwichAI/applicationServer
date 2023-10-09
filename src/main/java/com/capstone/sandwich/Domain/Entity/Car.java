package com.capstone.sandwich.Domain.Entity;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Car {

    @Id @Generated
    private Integer id;

    private String carNumber;
    private String videoUrl;

    /*TODO
    불량 유형 체크
     */

    private Integer scratch=0; //스크래치 개수
    private Integer installation=0; // 장착 불량 개수
    private Integer exterior=0; //외관 손상 개수
    private Integer gap=0; // 단차 손상 개수

    private Integer totalDefects=0;


}
