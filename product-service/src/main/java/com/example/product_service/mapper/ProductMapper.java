package com.example.product_service.mapper;

import com.example.product_service.dto.product.*;
import com.example.product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    @Mapping(target = "owner", ignore = true)
    ProductResponse toProductResponse(Product product);

    ProductInfoResponse toProductInfoResponse(Product product);

    Product toProduct (CreateProductRequest request);

    @Mapping(target = "id", ignore = true)
    void updateProduct (@MappingTarget Product product, UpdateProductRequest request);

    CheckProductResponse toCheckProductResponse(Product product);
}
