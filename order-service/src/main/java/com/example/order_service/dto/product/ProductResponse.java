package com.example.order_service.dto.product;

import com.example.order_service.dto.user.UserResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    Long id;
    String name;
    Long sellPrice;
    UserResponse owner;
}
