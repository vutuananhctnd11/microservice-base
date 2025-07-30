package com.example.product_service.mapper;

import com.example.product_service.dto.product.CreateProductRequest;
import com.example.product_service.dto.product.ProductInfoResponse;
import com.example.product_service.dto.product.ProductResponse;
import com.example.product_service.dto.product.UpdateProductRequest;
import com.example.product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    @Mapping(target = "owner", ignore = true)
    ProductResponse toProductResponse(Product product);

    ProductInfoResponse toProductInfoResponse(Product product);

    Product toProduct (CreateProductRequest request);

    void updateProduct (@MappingTarget Product product, UpdateProductRequest request);
}
