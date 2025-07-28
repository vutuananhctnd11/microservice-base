package com.example.user_service.dto.login;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {

    Boolean isAuthenticated;
    String username;
    String role;
    String accessToken;
}
