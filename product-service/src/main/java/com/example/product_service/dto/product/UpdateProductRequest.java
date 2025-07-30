package com.example.product_service.dto.product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {

    Long id;
    String name;
    String description;
    Long importPrice;
    Long sellPrice;
    Long quantity;
}
