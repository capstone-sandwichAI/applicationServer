package com.capstone.sandwich.service;

import com.capstone.sandwich.Domain.Entity.Car;
import com.capstone.sandwich.Repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;

    public Car getCar(String carNumber) {

        return carRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 차량 번호입니다."));
    }
}
