package com.example.order_service.service;

import com.example.order_service.dto.order.OrderItemResponse;
import com.example.order_service.dto.order.OrderItemRequest;
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

    public void createOrderItem(List<OrderItemRequest> request, Long orderId) {
        List<com.example.order_service.entity.OrderItem> items = request.stream().map(orderItemRequestRequest -> {
            com.example.order_service.entity.OrderItem orderItem = new com.example.order_service.entity.OrderItem();
            orderItem.setProductId(orderItemRequestRequest.getId());
            orderItem.setQuantity(orderItemRequestRequest.getQuantity());
            orderItem.setOrderId(orderId);
            return orderItem;
        }).toList();
        orderItemRepository.saveAll(items);
    }

    public void deleteOrderItem(Long orderId) {
        List<com.example.order_service.entity.OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        orderItemRepository.deleteAll(orderItems);
    }

    public List<OrderItemResponse> getOrderItems(Long orderId) {
        List<com.example.order_service.entity.OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return orderItems.stream().map( o-> {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setId(o.getId());
            orderItemResponse.setQuantity(o.getQuantity());
            orderItemResponse.setProductId(o.getProductId());
            return orderItemResponse;
        }).toList();
    }
}
