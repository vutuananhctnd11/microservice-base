package com.example.product_service.service;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.dto.UserResponse;
import com.example.product_service.entity.Product;
import com.example.product_service.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductService {

    ProductRepository repository;
    RestTemplate restTemplate;

    public ProductResponse getProductById(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(id);
        productResponse.setName(product.getName());
        productResponse.setSellPrice(product.getSellPrice());

        String userServiceUrl = "http://localhost:8081/users?id=" + product.getOwnerId();
        ResponseEntity<ApiResponse<UserResponse>> response = restTemplate.exchange(
                userServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<UserResponse>>() {}
        );
        UserResponse userResponse = Objects.requireNonNull(response.getBody()).getData();
        productResponse.setOwner(userResponse);

        return productResponse;
    }
}
