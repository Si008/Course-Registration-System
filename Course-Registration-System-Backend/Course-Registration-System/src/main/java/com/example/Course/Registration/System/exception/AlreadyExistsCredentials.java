package com.example.Course.Registration.System.exception;

public class AlreadyExistsCredentials extends RuntimeException {

    public AlreadyExistsCredentials(String message){
        super(message);
    }
}
