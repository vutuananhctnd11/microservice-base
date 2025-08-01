package com.example.order_service.dto.order;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOrderRequest {

    Long id;
    Long quantity;
}
