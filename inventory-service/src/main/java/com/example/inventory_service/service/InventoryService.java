package com.example.inventory_service.service;

import com.example.inventory_service.dto.event.CreateOrderEvent;
import com.example.inventory_service.dto.event.OrderItemEvent;
import com.example.inventory_service.entity.Inventory;
import com.example.inventory_service.entity.InventoryReservation;
import com.example.inventory_service.repository.InventoryRepository;
import com.example.inventory_service.repository.InventoryReservationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InventoryService {

    InventoryRepository inventoryRepository;
    InventoryReservationRepository inventoryReservationRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void createOrder(CreateOrderEvent event) {
        boolean success = true;

        for (OrderItemEvent item : event.getItems()) {
            boolean reserved = checkItemReserved(item.getProductId(), item.getQuantity(), event.getOrderId());
            if (!reserved) {
                success = false;
                break;
            }
        }

        if (!success) {
            cancelReservation(event.getOrderId());
            kafkaTemplate.send("order.inventory.failed", event);
            System.out.println("===========================FAILED TO CREATE ORDER");
            redisTemplate.opsForValue().set("order:" + event.getOrderId(), "FAILED", Duration.ofMinutes(10));
        } else {
            kafkaTemplate.send("order.inventory.confirmed", event);
            redisTemplate.opsForValue().set("order:" + event.getOrderId(), "CONFIRMED", Duration.ofMinutes(10));
        }
    }

    public boolean checkItemReserved(Long productId, Long quantity, Long orderId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(null);
        if (inventory == null) {log.error("Product " + productId + " not found"); return false;}

        long available = inventory.getQuantity() - inventory.getReserved();
        if (available < quantity) {
            return false;
        }

        //update reserved
        inventory.setReserved(inventory.getReserved() + quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);

        InventoryReservation inventoryReservation = new InventoryReservation();
        inventoryReservation.setProductId(productId);
        inventoryReservation.setQuantity(quantity);
        inventoryReservation.setOrderId(orderId);
        inventoryReservation.setStatus("RESERVED");
        inventoryReservationRepository.save(inventoryReservation);
        return true;
    }

    public void cancelReservation(Long orderId) {
        List<InventoryReservation> reservations = inventoryReservationRepository.findByOrderId(orderId);
        for (InventoryReservation res : reservations) {
            Inventory inventory = inventoryRepository.findByProductId(res.getProductId())
                    .orElse(null);
            if (inventory == null) {log.error("Product " + res.getProductId() + " not found"); return;}

            inventory.setReserved(inventory.getReserved() - res.getQuantity());
            inventoryRepository.save(inventory);

            res.setStatus("CANCELLED");
            inventoryReservationRepository.save(res);
        }
    }


}
