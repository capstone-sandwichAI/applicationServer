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
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Value("${AI_REQUEST_ENDPOINT}")
    private String ENDPOINT;

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
        AiResponseDTO response = getResponse(requestDTO);
        log.info("result scratch : {}, gap: {}, installation = {}, exterior = {}",response.getScratch(),response.getGap(),response.getInstallation(),response.getExterior());
        log.info("images = {}",response.getEncodedImages().size());
        return response;
    }

    //주 로직
    private AiResponseDTO getResponse(RequestDTO requestDTO){

        //이 자료형만 멀티파트로 하는데 가능하더라
        MultiValueMap<String, Object> requestBody = requestDTO.makeRequestForm();

        //request post문 작성
        AiResponseDTO responseDTO = buildWebClient().post()
                .uri("/v2/object-detection/best") //추가 url 넣어주고
                .body(BodyInserters.fromMultipartData(requestBody))//바디 넣는 부분
                .retrieve()
                .bodyToMono(AiResponseDTO.class)
                .block();

        responseDTO.setImageList();
        responseDTO.setCarNumber(requestDTO.getCarNumber());
        responseDTO.setTotalDefects();

        //TODO request에 대해서 실패한 경우 AiResponseDTO로 안 오는듯?
        return responseDTO;
    }

    //Web client 디폴트로 빌드하는 부분
    private WebClient buildWebClient() {


        //사진 크기 상관없게 해주는 역할
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
                .build();


        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(ENDPOINT) //엔드포인트
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)//멀티 파트로 설정
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