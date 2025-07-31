package com.example.order_service.controller;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.order.CreateOrderEvent;
import com.example.order_service.dto.order.CreateOrderRequest;
import com.example.order_service.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createOrder(@RequestBody CreateOrderRequest request) {
        orderService.createOrder(request);
        return new ResponseEntity<>(new ApiResponse<>(""), HttpStatus.OK);
    }
}
