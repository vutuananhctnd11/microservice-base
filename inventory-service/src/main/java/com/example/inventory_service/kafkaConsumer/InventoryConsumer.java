package com.example.inventory_service.kafkaConsumer;

import com.example.common_event_dto.CreateOrderEvent;
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

    @KafkaListener(topics = "inventory.update", groupId = "inventory-group")
    public void handleUpdateInventory (String message) {
        inventoryService.updateInventoryFromRedis(message);
    }

}
