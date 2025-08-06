package com.example.product_service.dto.product;

import com.example.product_service.validator.Min.MinConstraint;
import com.example.product_service.validator.NotBlank.NotBlankConstraint;
import com.example.product_service.validator.NotNull.NotNullConstraint;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @NotNullConstraint(name = "ID người tạo")
    Long ownerId;

    @NotBlankConstraint(name = "Tên sản phẩm")
    String name;

    String description;

    @MinConstraint(min = 0, name = "Giá nhập")
    Long importPrice;

    @MinConstraint(min = 0, name = "Giá bán")
    Long sellPrice;
}
