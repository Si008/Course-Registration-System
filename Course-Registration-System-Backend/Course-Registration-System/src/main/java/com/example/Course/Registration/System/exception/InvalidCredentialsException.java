package com.example.Course.Registration.System.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message){
        super(message);
    }
}
