package com.example.product_service.dto.product;

import com.example.product_service.validator.Min.MinConstraint;
import com.example.product_service.validator.NotBlank.NotBlankConstraint;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {

    @NotBlankConstraint(name = "ID sản phẩm")
    Long id;

    @NotBlankConstraint(name = "Tên sản phẩm")
    String name;

    String description;

    @MinConstraint(min = 0, name = "Giá nhập")
    Long importPrice;

    @MinConstraint(min = 0, name = "Giá bán")
    Long sellPrice;

    @MinConstraint(min = 0, name = "Số lượng")
    Long quantity;
}
