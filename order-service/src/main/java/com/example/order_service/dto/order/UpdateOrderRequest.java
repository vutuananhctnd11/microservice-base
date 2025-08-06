package com.example.order_service.dto.order;

import com.example.order_service.validator.NotNull.NotNullConstraint;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderRequest {

    @NotNullConstraint(name = "ID đơn hàng")
    Long id;

    @NotNullConstraint(name = "ID người đặt hàng")
    Long userId;

    List<OrderItemRequest> products;
}
