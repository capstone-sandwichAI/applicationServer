package com.capstone.sandwich.Controller;

import com.capstone.sandwich.Domain.DTO.RequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ApiController {

//    private final CarService

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
}
