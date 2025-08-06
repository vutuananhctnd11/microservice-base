package com.example.order_service.dto.order;

import com.example.order_service.entity.OrderItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

    Long id;
    Long userId;
    String status;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    List<OrderItemResponse> orderItems;
}
