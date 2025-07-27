package com.example.user_service.mapper;

import com.example.user_service.dto.user.CreateUserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserRequest request);

    UserResponse toUserResponse(User user);
}
