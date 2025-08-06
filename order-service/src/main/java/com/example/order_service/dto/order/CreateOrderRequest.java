package com.example.order_service.dto.order;

import com.example.order_service.validator.NotNull.NotNullConstraint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {

    @NotNullConstraint(name = "ID người đặt hàng")
    Long userId;

    List<OrderItemRequest> products;

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize InventoryReservedEvent", e);
        }
    }
}
