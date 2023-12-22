package com.example.librarysystemadmin.utils;

public class ApiResponse<T> {
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

    public void setErrorResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
