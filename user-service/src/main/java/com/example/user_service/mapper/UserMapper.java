package com.example.user_service.mapper;

import com.example.user_service.dto.user.CreateUserForAdminRequest;
import com.example.user_service.dto.user.CreateUserRequest;
import com.example.user_service.dto.user.UserResponse;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserRequest request);

//    @Mapping(target = "role", ignore = true)
    User toUser(CreateUserForAdminRequest request);

    UserResponse toUserResponse(User user);
}
