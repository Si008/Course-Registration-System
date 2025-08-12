package com.example.Course.Registration.System.exception;

public class AccountLockedException extends RuntimeException{

    public AccountLockedException(String message){
       super(message);
    }
}
