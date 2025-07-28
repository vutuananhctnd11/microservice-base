package com.example.user_service.controller;

import com.example.user_service.dto.ApiResponse;
import com.example.user_service.dto.login.LoginRequest;
import com.example.user_service.dto.login.LoginResponse;
import com.example.user_service.dto.user.CreateUserForAdminRequest;
import com.example.user_service.dto.user.CreateUserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login (@RequestBody LoginRequest loginRequest){
        ApiResponse<LoginResponse> response = new ApiResponse<>(userService.login(loginRequest));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser (@RequestBody CreateUserRequest request){
        ApiResponse<UserResponse> response = new ApiResponse<>(userService.createUser(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUserForAdmin (@RequestBody CreateUserForAdminRequest request){
        ApiResponse<UserResponse> response = new ApiResponse<>(userService.createUserForAdmin(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
