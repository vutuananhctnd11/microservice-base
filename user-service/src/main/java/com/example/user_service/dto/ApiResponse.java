package com.example.user_service.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    String message;
    String status;
    T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.status = "success";
    }

    public ApiResponse(T data) {
        this.data = data;
        this.status = "success";
        this.message = null;
    }
}
