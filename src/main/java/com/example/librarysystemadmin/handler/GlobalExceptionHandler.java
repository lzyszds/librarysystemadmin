package com.example.librarysystemadmin.handler;

import com.example.librarysystemadmin.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleMissingParameter(MissingServletRequestParameterException ex) {
        String errorMessage = "Required parameter '" + ex.getParameterName() + "' is missing";
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return apiResponse;
    }
}
