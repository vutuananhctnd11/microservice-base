package com.example.product_service.service;

import com.example.product_service.dto.ApiResponse;
import com.example.product_service.dto.PageableResponse;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
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
    public PageableResponse<ProductInfoResponse> getAllProducts(int page, int limit) {
        Pageable pageable = PageRequest.of(page-1, limit);
        Page<Product> products = productRepository.findAllByOrderByNameAsc(pageable);
        List<ProductInfoResponse> productInfoResponses = products.getContent()
                .stream()
                .map(productMapper::toProductInfoResponse)
                .toList();
        return PageableResponse.<ProductInfoResponse>builder()
                .page(page)
                .limit(limit)
                .totalPage((long) products.getTotalPages())
                .listResults(productInfoResponses)
                .build();
    }

    @Transactional
    public ProductInfoResponse createProduct(CreateProductRequest request) {
        Product product = productMapper.toProduct(request);
        product.setStatus("AVAILABLE");
        productRepository.save(product);
        redisTemplate.delete("productList");
        return productMapper.toProductInfoResponse(product);
    }

    @Transactional
    @CachePut(value = "products", key = "#request.id")
    public ProductInfoResponse updateProduct (UpdateProductRequest request) {
        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product, request);
        productRepository.save(product);
        redisTemplate.delete("productList");
        return productMapper.toProductInfoResponse(product);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    public List<CheckProductResponse> getListProductById (List<Long> ids) {
        List<Product> products = productRepository.getListProductsById(ids);
        System.out.println("PRODUCTS: " + products);
        return products.stream().map(productMapper::toCheckProductResponse).collect(Collectors.toList());
    }


    public List<ProductInfoResponse> parseExcel(MultipartFile file) {

        List<ProductInfoResponse> response = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Product product = new Product();
                product.setName(getCellString(row.getCell(0)));
                product.setDescription(getCellString(row.getCell(1)));
                product.setImportPrice((long) row.getCell(2).getNumericCellValue());
                product.setSellPrice((long) row.getCell(3).getNumericCellValue());

                productRepository.save(product);
                response.add(productMapper.toProductInfoResponse(product));
            }

        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file Excel", e);
        }

        return response;
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        return cell.getCellType() == CellType.STRING
                ? cell.getStringCellValue()
                : String.valueOf((long) cell.getNumericCellValue());
    }


}
