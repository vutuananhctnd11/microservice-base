package com.example.product_service.service;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.product.*;
import com.example.product_service.dto.UserResponse;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.CustomException;
import com.example.product_service.exception.ErrorCode;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductService {

    ProductRepository productRepository;
    WebClient.Builder webClientBuilder;
    RestTemplate restTemplate;
    ProductMapper productMapper;
    RedisTemplate<String, Object> redisTemplate;

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
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

        ProductResponse productResponse = productMapper.toProductResponse(product);
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

    @Cacheable(value = "productsList")
    public List<ProductInfoResponse> getAllProducts() {
        List<Product> products = productRepository.findAllByOrderByNameAsc();
        return products.stream().map(productMapper::toProductInfoResponse).toList();
    }

    public ProductInfoResponse createProduct(CreateProductRequest request) {
        Product product = productMapper.toProduct(request);
        productRepository.save(product);
        redisTemplate.delete("productList");
        return productMapper.toProductInfoResponse(product);
    }

    public ProductInfoResponse updateProduct (UpdateProductRequest request) {
        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product, request);
        productRepository.save(product);
        redisTemplate.delete("productList");
        return productMapper.toProductInfoResponse(product);
    }

    public List<CheckProductResponse> getListProductById (List<Long> ids) {
        List<Product> products = productRepository.getListProductsById(ids);
        System.out.println("PRODUCTS: " + products);
//        return products.stream().map(productMapper::toCheckProductResponse).collect(Collectors.toList());
        return products.stream().map( p ->
                CheckProductResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .status(p.getStatus())
                        .build()).collect(Collectors.toList());
    }


}
