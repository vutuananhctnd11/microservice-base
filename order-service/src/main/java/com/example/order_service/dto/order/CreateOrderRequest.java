package com.example.order_service.dto.order;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {

    Long userId;
    String address;
    List<ProductOrderRequest> products;
}
