package com.example.inventory_service.kafkaConsumer;

import com.example.inventory_service.dto.event.CreateOrderEvent;
import com.example.inventory_service.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryConsumer {

    InventoryService inventoryService;

    @KafkaListener(topics = "order.created", groupId = "inventory-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleOrderCreated(CreateOrderEvent event) {
        inventoryService.createOrder(event);
    }
}
