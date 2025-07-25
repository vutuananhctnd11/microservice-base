package com.example.product_service.controller;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@RequestParam Long id) {
        ApiResponse<ProductResponse> response = new ApiResponse<>(productService.getProductById(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
