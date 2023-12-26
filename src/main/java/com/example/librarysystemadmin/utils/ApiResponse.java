package com.example.librarysystemadmin.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiResponse<T> {
    public static final Logger log = LoggerFactory.getLogger(ApiResponse.class);

    private int code;
    private String message;
    private T data;

    public ApiResponse() {

    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public void setSuccessResponse(T data) {
        this.code = 200;
        this.message = "success";
        this.data = data;
    }

    public void setErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;

    }

    public void setErrorResponse(int code, String message, String api, Exception error) {
        this.code = code;
        this.message = message;
        this.data = null;
        log.error("api:《《 " + api + "》》 error: " + error +
                "\n----------------------------------------------------------------------------------------------------------------------------");
    }
}
