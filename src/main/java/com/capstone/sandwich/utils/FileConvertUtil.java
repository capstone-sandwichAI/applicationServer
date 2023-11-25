package com.capstone.sandwich.utils;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileConvertUtil {

    public static List<FileSystemResource> convertMultipartFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<FileSystemResource> fileSystemResources = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            FileSystemResource fileSystemResource = convertMultipartFile(multipartFile);
            fileSystemResources.add(fileSystemResource);
        }

        return fileSystemResources;
    }

    private static FileSystemResource convertMultipartFile(MultipartFile multipartFile) throws IOException {
        // Create a temporary file
        File tempFile = Files.createTempFile("temp", multipartFile.getOriginalFilename()).toFile();

        // Copy the contents of the MultipartFile to the temporary file
        Files.copy(multipartFile.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Create a FileSystemResource from the temporary file
        return new FileSystemResource(tempFile);
    }
}
