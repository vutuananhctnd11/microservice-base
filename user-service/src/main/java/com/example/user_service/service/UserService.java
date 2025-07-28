package com.example.user_service.service;

import com.example.user_service.dto.login.LoginRequest;
import com.example.user_service.dto.login.LoginResponse;
import com.example.user_service.dto.user.CreateUserForAdminRequest;
import com.example.user_service.dto.user.CreateUserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.exception.CustomException;
import com.example.user_service.exception.ErrorCode;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.JwtUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    RoleRepository roleRepository;


    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFullName(user.getFullName());
        return userResponse;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
    public UserResponse createUserForAdmin(CreateUserForAdminRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findById(Long.valueOf(request.getRole()))
                .orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND));
        user.setRole(role);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        boolean checkPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!checkPassword) {
            throw new CustomException(ErrorCode.PASSWORD_INVALID);
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .isAuthenticated(true)
                .username(user.getUsername())
                .role(user.getRole().getRoleName())
                .build();
    }

}
