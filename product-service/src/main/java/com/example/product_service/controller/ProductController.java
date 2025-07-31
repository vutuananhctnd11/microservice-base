package com.example.product_service.controller;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.product.*;
import com.example.product_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @GetMapping(params = "id")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@RequestParam Long id) {
        ApiResponse<ProductResponse> response = new ApiResponse<>(productService.getProductById(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductInfoResponse>>> getAllProducts() {
        ApiResponse<List<ProductInfoResponse>> response = new ApiResponse<>(productService.getAllProducts());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductInfoResponse>> createProduct(@RequestBody CreateProductRequest request) {
        ApiResponse<ProductInfoResponse> response = new ApiResponse<>(productService.createProduct(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProductInfoResponse>> updateProduct(@RequestBody UpdateProductRequest request) {
        ApiResponse<ProductInfoResponse> response = new ApiResponse<>(productService.updateProduct(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/search-list")
    public ResponseEntity<ApiResponse<List<CheckProductResponse>>> searchListProduct (@RequestBody List<Long> ids) {
        ApiResponse<List<CheckProductResponse>> response = new ApiResponse<>(productService.getListProductById(ids));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
