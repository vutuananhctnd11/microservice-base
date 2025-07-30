package com.example.product_service.dto.product;

import com.example.product_service.dto.UserResponse;
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
