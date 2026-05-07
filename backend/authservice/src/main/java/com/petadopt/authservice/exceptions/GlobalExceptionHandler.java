package com.petadopt.authservice.exceptions;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String,String> errorMap = new HashMap();
        e.getBindingResult().getAllErrors().forEach(error->{
            String fieldName = ((FieldError)error).getField();
            String msg = error.getDefaultMessage();
            errorMap.put(fieldName , msg);
        });
        return errorMap;
    }

    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", e.getMessage());
        return errorMap;
    }

    @ExceptionHandler(Exception.class)
    public Map<String, String> handleException(Exception e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", "An unexpected error occurred: " + e.getMessage());
        return errorMap;
    }
}
