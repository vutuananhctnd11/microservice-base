package com.example.order_service.dto.product;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckProductResponse {

    Long id;
    String name;
    String status;
}
