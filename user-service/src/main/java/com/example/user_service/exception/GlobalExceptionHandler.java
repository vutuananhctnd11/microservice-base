package com.example.user_service.exception;

import com.example.user_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException exception) {
        ApiResponse<Object> apiResponse = new ApiResponse<>("error", "RuntimeException: " + exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CustomException.class)
    ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Object> apiResponse = new ApiResponse<>("error", errorCode.getMessage());
        return new ResponseEntity<>(apiResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse<Object>> handleAccessDenied(AuthorizationDeniedException exception) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(ErrorCode.UNAUTHORIZED.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }
}
