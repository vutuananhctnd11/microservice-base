package com.example.product_service.dto.product;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CheckProductResponse {

    Long id;
    String name;
    String status;
}
