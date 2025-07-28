package com.example.user_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    USER_NOT_FOUND ("Không tồn tại người dùng trong hệ thống!", HttpStatus.NOT_FOUND),
    PASSWORD_INVALID ("Mật khẩu nhập vào không đúng!", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND ("Không tồn tại phân quyền này trong hệ thống!", HttpStatus.NOT_FOUND),
    USER_EXISTED("Người dùng đã tồn tại trong hệ thống!", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("Bạn không có quyền sử dụng chức năng này!", HttpStatus.UNAUTHORIZED),
    ;

    String message;
    HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

