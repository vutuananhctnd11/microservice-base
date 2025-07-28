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
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductService {

    ProductRepository repository;
    WebClient.Builder webClientBuilder;
    RestTemplate restTemplate;

    public ProductResponse getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ApiResponse<UserResponse> apiResponse = webClientBuilder.build().get()
                .uri("http://user-service/users?id=" + product.getOwnerId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserResponse>>() {})
                .block();

//        ApiResponse<UserResponse> apiResponse = useRestTemplate(product.getOwnerId());

        if (apiResponse == null || apiResponse.getData() == null) {
            throw new RuntimeException("User not found from user-service");
        }

        UserResponse userResponse = apiResponse.getData();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(id);
        productResponse.setName(product.getName());
        productResponse.setSellPrice(product.getSellPrice());
        productResponse.setOwner(userResponse);

        return productResponse;
    }

    private ApiResponse<UserResponse> useRestTemplate(Long ownerId) {
        ResponseEntity<ApiResponse<UserResponse>> response = restTemplate.exchange(
                "http://user-service/users?id=" + ownerId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<UserResponse>>() {}
        );
        return response.getBody();
    }
}
