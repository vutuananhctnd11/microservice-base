package com.example.user_service.controller;

import com.example.user_service.dto.ApiResponse;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getUserById (@RequestParam Long id){
        ApiResponse<UserResponse> response = new ApiResponse<>(userService.getUserById(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
