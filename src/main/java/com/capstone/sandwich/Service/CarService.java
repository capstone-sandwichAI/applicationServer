package com.capstone.sandwich.Service;

import com.capstone.sandwich.Domain.DTO.AiResponseDTO;
import com.capstone.sandwich.Domain.DTO.BackResponseDTO;
import com.capstone.sandwich.Domain.DTO.ReportDto;
import com.capstone.sandwich.Domain.DTO.RequestDTO;
import com.capstone.sandwich.Domain.Entity.Car;
import com.capstone.sandwich.Domain.Entity.CarImages;
import com.capstone.sandwich.Domain.Exception.ApiException;
import com.capstone.sandwich.Repository.CarImagesRepository;
import com.capstone.sandwich.Repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarImagesRepository carImagesRepository;

    public Integer validateDTO(RequestDTO dto) throws ApiException {
        log.info("Validation start");
        List<MultipartFile> imageList = dto.getImageList();
        String carNumber = dto.getCarNumber();

        //사진 개수 검사
//        if (imageList.size() == 0) {
//            throw new ApiException(HttpStatus.NOT_ACCEPTABLE, ApiException.NotAllowed());
//        }

        //차량 번호 중복 검사
        if (isDuplicate(carNumber)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ApiException.duplicateCarNumberException());
        }

        //미디어 타입 검사
        List<MultipartFile> unsupportedFiles = imageList.stream().filter(
                file -> (!file.getContentType().equals("image/png")&&!file.getContentType().equals("image/jpeg"))
        ).collect(Collectors.toList());

        //형식 위반 파일 있는 경우
        if (unsupportedFiles.size() > 0)
            throw new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,ApiException.MediaTypeException(unsupportedFiles.toString()));

        log.info("Validation Success");
        return unsupportedFiles.size();
    }

    public AiResponseDTO requestToAi(RequestDTO requestDTO) {
        log.info("request to Ai");
        //TODO HTTPRequest to Ai

        return AiResponseDTO.builder()
                .carNumber(requestDTO.getCarNumber())
                .imageList(requestDTO.getImageList())
                .exterior(0)
                .gap(0)
                .installation(0)
                .scratch(0)
                .totalDefects(0)
                .build();
    }

    public void insertDB(AiResponseDTO aiResponseDTO, List<String> imageUrlList) {
        //car Entity 생성
        Car car = Car.builder()
                .carNumber(aiResponseDTO.getCarNumber())
                .exterior(aiResponseDTO.getExterior())
                .installation(aiResponseDTO.getInstallation())
                .gap(aiResponseDTO.getGap())
                .scratch(aiResponseDTO.getScratch())
                .totalDefects(aiResponseDTO.getTotalDefects())
                .createdDate(LocalDate.now())
                .build();

        //url list 기반으로 CarImages 객체 리스트
        List<CarImages> images = imageUrlList.stream().map((url) -> {
            return CarImages.builder()
                    .car(car)
                    .imageUrl(url)
                    .build();
        }).collect(Collectors.toList());

        car.setCarImages(images);

        //저장
        carRepository.save(car);
        carImagesRepository.saveAll(images);
    }

    public List<Car> findCarThisMonth(int year, int month) {
        return carRepository.findByThisMonth(year, month);
    }

    //main 로직에서 추가
    public Car getCar(String carNumber) {

        return carRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 차량 번호입니다."));
    }

    public List<String> getCarImagesUrl(Integer id) {
        Optional<Car> carOptional = carRepository.findById(id);

        if (carOptional.isEmpty()) {
            throw new EntityNotFoundException("Id값에 대한 차량이 존재하지 않습니다.");
        }

        Car car = carOptional.get();
        List<CarImages> carImagesList = car.getCarImages();

        if (carImagesList.isEmpty()) {
            throw new EntityNotFoundException("차량 이미지가 존재하지 않습니다.");
        }

        return carImagesList.stream()
                .map(CarImages::getImageUrl)
                .collect(Collectors.toList());
    }

    public BackResponseDTO convertToBackResponseDto(Car car, Integer id) {
        List<String> imageUrlList = getCarImagesUrl(id);

        return BackResponseDTO.builder()
                .carNumber(car.getCarNumber())
                .exterior(car.getExterior())
                .scratch(car.getScratch())
                .installation(car.getInstallation())
                .gap(car.getGap())
                .totalDefects(car.getTotalDefects())
                .imageUrlList(imageUrlList)
                .build();
    }



    public List<ReportDto> getReportDtoFromDate(LocalDate date) {
        List<Car> carList = carRepository.findByCreatedDate(date);

        List<ReportDto> reportDtoList = carList.stream()
                .map(this::ConvertToReportDto)
                .collect(Collectors.toList());

        return reportDtoList;
    }

    private ReportDto ConvertToReportDto(Car car) {
        return ReportDto.builder()
                .carNumber(car.getCarNumber())
                .scratch(car.getScratch())
                .gap(car.getGap())
                .exterior(car.getExterior())
                .installation(car.getInstallation())
                .totalDefects(car.getTotalDefects())
                .createdDate(car.getCreatedDate())
                .build();
    }

    public boolean isDuplicate(String carNumber) {
        if (carRepository.findByCarNumber(carNumber).isEmpty()) {
            return false;
        }
        return true;
    }
}