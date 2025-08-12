package com.example.Course.Registration.System.util;

public class ApiResponseUtil<T> {

    private String result;
    private boolean success;
    private String message;
    private int statusCode;
    private T data;


    public ApiResponseUtil(String result, boolean success, String message,int statusCode, T data) {
        this.result = result;
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public ApiResponseUtil() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
