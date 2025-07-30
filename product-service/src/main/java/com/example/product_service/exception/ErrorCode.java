package com.example.product_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    PRODUCT_NOT_FOUND ("Không tồn tại Sản phẩm này trong hệ thống!", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("Bạn không có quyền sử dụng chức năng này!", HttpStatus.UNAUTHORIZED),
    ;

    String message;
    HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

