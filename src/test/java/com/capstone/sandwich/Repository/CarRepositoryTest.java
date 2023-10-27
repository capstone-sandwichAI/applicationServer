package com.capstone.sandwich.Repository;

import com.capstone.sandwich.Domain.Entity.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void cleanup(){
        carRepository.deleteAll();
    }

//    @Test
//    public void 데이터가_없을_때_반환_테스트(){
//        //give
//        List<Car> carList = carRepository.findAll();
//
//        //when
//
//        //then
//        assertTrue(carList.isEmpty());
//    }

    @Test
    @Transactional
    public void 데이터_삽입_조회(){
        //give
        Car car = Car.builder()
                .carNumber("abc123")
                .gap(0)
                .scratch(2)
                .exterior(1)
                .installation(0)
                .totalDefects(3)
                .createdDate(LocalDate.now())
                .build();
        carRepository.save(car);

        //when
        List<Car> carList = carRepository.findAll();

        //then
        Car savedCar = carList.get(0);
        assertThat(savedCar.getCarNumber()).isEqualTo("abc123");
        assertThat(savedCar.getScratch()).isEqualTo(car.getScratch());
        assertThat(savedCar.getCreatedDate()).isEqualTo(car.getCreatedDate());
    }

}
