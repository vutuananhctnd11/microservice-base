package com.example.order_service.controller;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.order.CreateOrderRequest;
import com.example.order_service.dto.order.OrderResponse;
import com.example.order_service.dto.order.OrderStatusResponse;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        orderService.createOrder(request);
        return new ResponseEntity<>(new ApiResponse<>("", null), HttpStatus.OK);
    }

    @DeleteMapping("/topic/{name}")
    public String deleteTopic(@PathVariable String name) {
        orderService.deleteTopic(name);
        return "Requested deletion of topic: " + name;
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<OrderStatusResponse>> getStatus(@RequestParam Long orderId) {
        ApiResponse<OrderStatusResponse> response = new ApiResponse<>(orderService.getOrderStatus(orderId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(params = "orderId")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@RequestParam Long orderId) {
        ApiResponse<OrderResponse> response = new ApiResponse<>(orderService.getOrder(orderId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
