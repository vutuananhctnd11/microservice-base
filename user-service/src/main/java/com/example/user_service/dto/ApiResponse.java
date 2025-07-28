package com.example.user_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
