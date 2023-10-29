package com.capstone.sandwich.Domain.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiException extends Exception{
    private HttpStatus code;
    private String message;

    public static String NullException() {return "No Files";}

    public static String MediaTypeException(String fileName) {
        return fileName + " unsupported media type";
    }
}
