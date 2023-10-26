package com.capstone.sandwich.Repository;

import com.capstone.sandwich.Domain.Entity.Car;
import com.capstone.sandwich.Domain.Entity.CarImages;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CarImagesRepositoryTest {

    @Autowired
    private CarImagesRepository carImagesRepository;

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void cleanUp(){
        carRepository.deleteAll();
        carImagesRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 차량_사진_레포_데이터_삽입_조회(){
        //given
        Car car = Car.builder()
                .carNumber("abcganada123")
                .gap(1)
                .scratch(1)
                .installation(0)
                .exterior(0)
                .totalDefects(2)
                .createdDate(LocalDate.now())
                .build();
        carRepository.save(car);

        CarImages carImages1 = CarImages.builder()
                .imageUrl("example.com/1")
                .car(car)
                .build();
        carImagesRepository.save(carImages1);

        CarImages carImages2 = CarImages.builder()
                .imageUrl("example.com/2")
                .car(car)
                .build();
        carImagesRepository.save(carImages2);

        //when
        List<Car> carList = carRepository.findAll();
        Car findCar = carList.get(0);
        List<CarImages> findCarImages = findCar.getCarImages();

        //then
        if(findCarImages != null) {
            assertThat(findCarImages.size()).isEqualTo(2);
            assertThat(findCarImages.get(0).getCar()).isEqualTo(findCar);
            assertThat(findCarImages.get(0).getImageUrl()).isEqualTo("example.com/1");
            assertThat(findCarImages.get(1).getCar()).isEqualTo(findCar);
            assertThat(findCarImages.get(1).getImageUrl()).isEqualTo("example.com/2");
        } else{
            assertFalse(false);
        }
    }
}
