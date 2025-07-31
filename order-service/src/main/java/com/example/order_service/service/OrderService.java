package com.example.order_service.service;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.order.CreateOrderEvent;
import com.example.order_service.dto.order.CreateOrderRequest;
import com.example.order_service.dto.order.OrderItemEvent;
import com.example.order_service.dto.order.ProductOrderRequest;
import com.example.order_service.dto.product.CheckProductResponse;
import com.example.order_service.dto.user.UserResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    OrderRepository orderRepository;
    OrderItemService orderItemService;
    WebClient.Builder webClientBuilder;
    RestTemplate restTemplate;
    KafkaTemplate<Object, CreateOrderEvent> kafkaTemplate;

    public void createOrder(CreateOrderRequest request){
        //check user
        ApiResponse<UserResponse> checkUser = webClientBuilder.build().get()
                .uri("http://user-service/users?id="+request.getUserId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserResponse>>() {})
                .block();

        if (checkUser == null || checkUser.getData() == null) {
            throw new RuntimeException("User not found from user-service");
        }

        //check product
        List<Long> productIds = request.getProducts().stream().map(ProductOrderRequest::getId).toList();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestToProductService = new HttpEntity<>(productIds, headers);
        ResponseEntity<ApiResponse<List<CheckProductResponse>>> checkProducts = restTemplate.exchange(
                "http://product-service/products/search-list",
                HttpMethod.POST,
                requestToProductService,
                new ParameterizedTypeReference<ApiResponse<List<CheckProductResponse>>>() {} );

        List<CheckProductResponse> checkProductResponses = Objects.requireNonNull(checkProducts.getBody()).getData();
        List<CheckProductResponse> unavailableProducts = checkProductResponses.stream()
                .filter( p -> !p.getStatus().equalsIgnoreCase("AVAILABLE"))
                .toList();

        if (!unavailableProducts.isEmpty()) {
            String names = unavailableProducts.stream()
                    .map(CheckProductResponse::getName)
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Product with name " + names + " is unavailable");
        }

        //save to database
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("CREATED");
        order.setCreateAt(LocalDateTime.now());
        orderRepository.save(order);
        orderItemService.createOrderItem(request.getProducts(), order.getId());

        //kafka
        CreateOrderEvent event = new CreateOrderEvent();
        event.setOrderId(order.getId());
        event.setUserId(request.getUserId());
        event.setItems(request.getProducts().stream()
                .map(p -> new OrderItemEvent(p.getId(), p.getQuantity()))
                .toList());

        kafkaTemplate.send("order.created", event);
    }


}
