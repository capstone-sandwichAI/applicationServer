package com.capstone.sandwich.Controller;

import com.capstone.sandwich.Domain.DTO.AiResponseDTO;
import com.capstone.sandwich.Domain.DTO.BackResponseDTO;
import com.capstone.sandwich.Domain.DTO.RequestDTO;
import com.capstone.sandwich.Domain.DTO.TestDTO;
import com.capstone.sandwich.Domain.Exception.ApiException;
import com.capstone.sandwich.Service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.capstone.sandwich.Domain.Entity.Car;

import org.springframework.http.HttpStatus;
import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final CarService carService;

    @PostMapping("/inspection/{carNumber}")
    public void frontRequest(@ModelAttribute RequestDTO requestDTO, @PathVariable("carNumber") String carNumber) throws ApiException {
        requestDTO.setCarNumber(carNumber);
        log.info("request car = {}, image cnt = {}", requestDTO.getCarNumber(), requestDTO.getPhotos().size());

        //validation
        carService.validateDTO(requestDTO);
        //request to Ai - input requestDTO
        //response from Ai - output AiResponseDTO
        AiResponseDTO aiResponseDTO = carService.requestToAi(requestDTO);

        //insert Storage - input AiResponseDTO.getPhotos() output url List
        List<String> urls = carService.insertStorage(aiResponseDTO.getPhotos());

        //insert DB - input AiResponseDTO
        carService.insertDB(aiResponseDTO, urls);

        //make Report - input Car output string


    }

    @PostMapping("/file")
    public String estimateSpeed(@ModelAttribute TestDTO dto) {
        return "Success";
    }

    @GetMapping("/test")
    public ResponseEntity<String> testApi(){
        return ResponseEntity.status(HttpStatus.OK).body("connection between fe and be is successful");
    }

    @GetMapping("/dummy-data/{carNumber}")
    public ResponseEntity<?> dummyDataApi(@PathVariable("carNumber") String carNumber) {

        try{
            Car car = carService.getCar(carNumber);
            BackResponseDTO backResponseDTO = carService.convertToDto(car, car.getId());

            return ResponseEntity.status(HttpStatus.OK).body(backResponseDTO);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
