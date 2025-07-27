package com.example.user_service.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {

    String username;
    String password;
    String fullName;
    Integer age;
}
