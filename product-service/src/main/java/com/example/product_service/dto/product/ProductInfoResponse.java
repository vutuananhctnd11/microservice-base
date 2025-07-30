package com.example.product_service.dto.product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInfoResponse {

    Long id;
    String name;
    Long quantity;
    Long importPrice;
    Long sellPrice;
    String description;

}
