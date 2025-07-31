package com.example.order_service.service;

import com.example.order_service.dto.order.ProductOrderRequest;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.repository.OrderItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemService {

    OrderItemRepository orderItemRepository;

    public void createOrderItem(List<ProductOrderRequest> request, Long orderId) {
        List<OrderItem> items = request.stream().map(orderItemRequest -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderItemRequest.getId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setOrderId(orderId);
            return orderItem;
        }).toList();
        orderItemRepository.saveAll(items);
    }
}
