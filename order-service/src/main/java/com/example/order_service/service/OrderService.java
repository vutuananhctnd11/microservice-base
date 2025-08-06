package com.example.order_service.service;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.order.*;
import com.example.order_service.dto.product.CheckProductResponse;
import com.example.order_service.dto.user.UserResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OutboxEvent;
import com.example.order_service.exception.CustomException;
import com.example.order_service.exception.ErrorCode;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.repository.OutboxEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    KafkaTemplate<Object, Object> kafkaTemplate;
    RedisTemplate<String, Object> redisTemplate;
    OutboxEventRepository outboxEventRepository;

    public void createOrder(CreateOrderRequest request) {
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
        List<Long> productIds = request.getProducts().stream().map(OrderItemRequest::getId).toList();
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

        //check inventory
        HttpEntity<List<OrderItemRequest>> requestToInventoryService = new HttpEntity<>(request.getProducts(), headers);
        ResponseEntity<ApiResponse<Boolean>> checkInventory = restTemplate.exchange(
                "http://inventory-service/inventory",
                HttpMethod.POST,
                requestToInventoryService,
                new ParameterizedTypeReference<ApiResponse<Boolean>>() {} );

        boolean inventoryResponse = Objects.requireNonNull(checkInventory.getBody()).getData();

        if (!inventoryResponse) {
            throw new CustomException((ErrorCode.QUANTITY_NOT_ENOUGH));
        }

        //save to database
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("CREATED");
        order.setCreateAt(LocalDateTime.now());
        orderRepository.save(order);
        orderItemService.createOrderItem(request.getProducts(), order.getId());

        // Save order created event to outbox
        OutboxEvent event = new OutboxEvent("order_created", request.toJson());
        outboxEventRepository.save(event);
    }

    KafkaAdmin kafkaAdmin;

    public void deleteTopic(String topicName) {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            adminClient.deleteTopics(List.of(topicName)).all().get();
            System.out.println("Deleted topic: " + topicName);
        } catch (Exception e) {
            System.err.println("Failed to delete topic: " + topicName);
            e.printStackTrace();
        }
    }

    public OrderStatusResponse getOrderStatus(Long orderId) {
        OrderStatusResponse response = new OrderStatusResponse();
        response.setId(orderId);
        String status = (String) redisTemplate.opsForValue().get("order-status:"+orderId);
        if (status != null) {
            response.setStatus(status);
        } else {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
            response.setStatus(order.getStatus());
        }
        return response;
    }

    @Cacheable(value = "order", key = "#id")
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        List<OrderItemResponse> orderItems = orderItemService.getOrderItems(id);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(id);
        orderResponse.setUserId(order.getUserId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setCreateAt(order.getCreateAt());
        orderResponse.setUpdateAt(order.getUpdateAt());
        orderResponse.setOrderItems(orderItems);
        return orderResponse;
    }

    @Transactional
    public OrderResponse updateOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return null;
    }

}
