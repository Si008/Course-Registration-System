package com.example.Course.Registration.System.exception;

import com.example.Course.Registration.System.util.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BlankFieldException.class)
    public ResponseEntity<ApiResponseUtil<?>> handleBlankField(BlankFieldException ex) {
        ApiResponseUtil<?> response = new ApiResponseUtil<>(
                "FAILURE", false, ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseUtil<?>> handleNotFound(ResourceNotFoundException ex) {
        ApiResponseUtil<?> response = new ApiResponseUtil<>(
                "FAILURE", false, ex.getMessage(),
                HttpStatus.NOT_FOUND.value(), null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseUtil<?>> handleGeneric(Exception ex) {
        ApiResponseUtil<?> response = new ApiResponseUtil<>(
                "FAILURE", false, "Unexpected error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(), null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiResponseUtil<?>> handleAccountLocked(AccountLockedException ex) {
        ApiResponseUtil<?> response = new ApiResponseUtil<>();
        response.setResult("FAILURE");
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setStatusCode(HttpStatus.LOCKED.value());
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.LOCKED);
    }

    @ExceptionHandler(AlreadyExistsCredentials.class)
    public ResponseEntity<ApiResponseUtil<?>> alreadyCredentialsAvailable(AlreadyExistsCredentials ex){
        ApiResponseUtil<?> response  = new ApiResponseUtil<>();
        response.setResult("FAILURE");
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setStatusCode(HttpStatus.CONFLICT.value());
        response.setData(null);
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }
}

