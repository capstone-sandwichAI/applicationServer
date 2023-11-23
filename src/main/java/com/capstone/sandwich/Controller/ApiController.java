package com.capstone.sandwich.Controller;

import com.capstone.sandwich.Domain.DTO.*;
import com.capstone.sandwich.Domain.Entity.CarImages;
import com.capstone.sandwich.Domain.Exception.ApiException;
import com.capstone.sandwich.Service.CarService;
import com.capstone.sandwich.aws.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.capstone.sandwich.Domain.Entity.Car;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final CarService carService;
    private final S3Service s3Service;

    @PostMapping("/inspection")
    public ResponseEntity<?> requestInspection(@ModelAttribute RequestDTO requestDTO) throws ApiException, IOException {
        log.info("request car = {}, image cnt = {}", requestDTO.getCarNumber(), requestDTO.getImageList().size());


        //validation
        carService.validateDTO(requestDTO);

        //request to Ai - input requestDTO

        //response from Ai - output AiResponseDTO
        AiResponseDTO aiResponseDTO = carService.requestToAi(requestDTO);

        //insert Storage - input AiResponseDTO.getPhotos() output url List
        List<MultipartFile> imageList = requestDTO.getImageList();
        List<String> imageUrlList = new ArrayList<>();
        for (MultipartFile image : imageList) {
            String imageUrl = s3Service.upload(image);
            imageUrlList.add(imageUrl);
        }

        //insert DB - input AiResponseDTO
        carService.insertDB(aiResponseDTO, imageUrlList);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/file")
    public String estimateSpeed(@ModelAttribute TestDTO dto) {
        return "Success";
    }

    @GetMapping("/test")
    public ResponseEntity<String> testApi(){
        return ResponseEntity.status(HttpStatus.OK).body("connection between fe and be is successful");
    }

    @GetMapping("/inspection/result/{carNumber}")
    public ResponseEntity<?> readCarInfo(@PathVariable("carNumber") String carNumber) {

        try{
            Car car = carService.getCar(carNumber);
            BackResponseDTO backResponseDTO = carService.convertToBackResponseDto(car, car.getId());

            return ResponseEntity.status(HttpStatus.OK).body(backResponseDTO);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/report/{date}")
    public ResponseEntity<?> readReportFromDate(@PathVariable("date") String date){
        //date: ex) 2023-11-11
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        List<ReportDto> reportList = carService.getReportDtoFromDate(localDate);

        if(reportList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 날짜에 진행된 검수가 없습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(reportList);
    }

}
