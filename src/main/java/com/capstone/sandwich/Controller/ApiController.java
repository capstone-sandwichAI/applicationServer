package com.capstone.sandwich.Controller;

import com.capstone.sandwich.Domain.DTO.RequestDTO;
import com.capstone.sandwich.Domain.Entity.Car;
import com.capstone.sandwich.service.CarService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final CarService carService;

    @PostMapping("/inspection/{carNumber}")
    public void frontRequest(@ModelAttribute RequestDTO requestDTO, @PathVariable("carNumber") String carNumber) {
        requestDTO.setCarNumber(carNumber);
        log.info("request car = {}, image cnt = {}",requestDTO.getCarNumber(),requestDTO.getPhotos().size());

        //validation

        //request to Ai

        //response from Ai

        //insert DB

        //make Report
    }

    @GetMapping("/test")
    public ResponseEntity<String> testApi(){
        return ResponseEntity.status(HttpStatus.OK).body("connection between fe and be is successful");
    }

    @GetMapping("/dummy-data/{carNumber}")
    public ResponseEntity<?> dummyDataApi(@PathVariable("carNumber") String carNumber) {

        try{
            Car car = carService.getCar(carNumber);
            return ResponseEntity.status(HttpStatus.OK).body(car);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
