package com.capstone.sandwich.Service;

import com.capstone.sandwich.Domain.DTO.AiResponseDTO;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarImagesRepository carImagesRepository;

    public Integer validateDTO(RequestDTO dto) throws ApiException {
        log.info("Validation start");
        List<MultipartFile> photos = dto.getPhotos();

        //dto에 field마다 null이 아닌지와 사진은 8장인지
        if(dto.getCarNumber().isEmpty()||photos.size()!=8)
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,ApiException.NotAllowed());

        //미디어 타입 검사
        List<MultipartFile> unsupportedFiles = photos.stream().filter(
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

        return new AiResponseDTO();
    }

    public List<String> insertStorage(List<MultipartFile> photos) {
        ArrayList<String> urls = new ArrayList<>();
        //TODO 스토리지 저장 및 url 추출
        return urls;
    }
    public void insertDB(AiResponseDTO aiResponseDTO,List<String> urls) {

        //url list 기반으로 CarImages 객체 리스트
        List<CarImages> images = urls.stream().map((url) -> {
            return CarImages.builder().imageUrl(url).build();
        }).collect(Collectors.toList());

        //car Entity 생성
        Car car = Car.builder()
                .carNumber(aiResponseDTO.getCarNumber())
                .exterior(aiResponseDTO.getExterior())
                .installation(aiResponseDTO.getInstallation())
                .gap(aiResponseDTO.getGap())
                .scratch(aiResponseDTO.getScratch())
                .totalDefects(aiResponseDTO.getTotalDefects())
                .createdDate(LocalDate.now())
                .carImages(images)
                .build();
        //저장
        carRepository.save(car);
    }

    public List<Car> findCarThisMonth(int year, int month) {
        return carRepository.findByThisMonth(year, month);
    }

    //main 로직에서 추가
    public Car getCar(String carNumber) {

        return carRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 차량 번호입니다."));
    }
}