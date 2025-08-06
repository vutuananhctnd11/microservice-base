package com.example.order_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    ORDER_NOT_FOUND ("Không tồn tại đơn hàng này trong hệ thống!", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("Bạn không có quyền sử dụng chức năng này!", HttpStatus.UNAUTHORIZED),
    FIELD_NOT_BLANK("{name} không được để trống!", HttpStatus.BAD_REQUEST),
    INVALID_SIZE_MIN("{name} phải có giá trị lớn hơn {min}!", HttpStatus.BAD_REQUEST),
    QUANTITY_NOT_ENOUGH("Số lượng tồn của sản phẩm không đủ!", HttpStatus.BAD_REQUEST),
    ;

    String message;
    HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

