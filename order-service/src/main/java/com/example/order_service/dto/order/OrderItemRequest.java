package com.example.order_service.dto.order;

import com.example.order_service.validator.Min.MinConstraint;
import com.example.order_service.validator.NotNull.NotNullConstraint;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {

    @NotNullConstraint(name = "ID sản phẩm")
    Long id;

    @MinConstraint(name = "Số lượng sản phẩm",min = 1)
    Long quantity;
}
