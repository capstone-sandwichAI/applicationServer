package com.capstone.sandwich.Service;

import com.capstone.sandwich.Domain.DTO.AiResponseDTO;
import com.capstone.sandwich.Domain.DTO.RequestDTO;
import com.capstone.sandwich.Domain.Exception.ApiException;
import com.capstone.sandwich.Repository.CarImagesRepository;
import com.capstone.sandwich.Repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        if(photos.isEmpty()||photos.get(0).isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,ApiException.NullException());

        List<MultipartFile> unsupportedFiles = photos.stream().filter(
                file -> (!file.getContentType().equals("image/png")) //jpeg도 포함 0
        ).collect(Collectors.toList());

        if (unsupportedFiles.size() > 0)
            throw new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,ApiException.MediaTypeException(unsupportedFiles.toString()));

        log.info("Validation Success");
        return unsupportedFiles.size();
    }

    public AiResponseDTO requestToAi(RequestDTO requestDTO) {

        //HTTPRequest to Ai

        return new AiResponseDTO();
    }

    public void insertStorage(List<MultipartFile> photos) {

    }

}
