package com.example.order_service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    String status;
    String message;
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

    public ApiResponse(String message) {
        this.message = message;
        this.status = "error";
        this.data = null;
    }
}
