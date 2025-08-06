package com.example.inventory_service.service;

import com.example.common_event_dto.CreateOrderEvent;
import com.example.common_event_dto.OrderItemEvent;
import com.example.inventory_service.dto.InventoryReservedEvent;
import com.example.inventory_service.dto.OrderItem;
import com.example.inventory_service.entity.Inventory;
import com.example.inventory_service.entity.InventoryReservation;
import com.example.inventory_service.entity.OutboxEvent;
import com.example.inventory_service.repository.InventoryRepository;
import com.example.inventory_service.repository.InventoryReservationRepository;
import com.example.inventory_service.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InventoryService {

    InventoryRepository inventoryRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    RedisTemplate<String, Object> redisTemplate;
    OutboxEventRepository outboxEventRepository;
    ObjectMapper objectMapper;

    public boolean checkAndReserve(List<OrderItem> items) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText("""
                for i = 1, #KEYS do
                    local stock = tonumber(redis.call('get', KEYS[i]))
                    local quantity = tonumber(ARGV[i])
                    if stock == nil or stock < quantity then
                        return 0
                    end
                end
                for i = 1, #KEYS do
                    local quantity = tonumber(ARGV[i])
                    redis.call('decrby', KEYS[i], quantity)
                end
                return 1
                """);
        redisScript.setResultType(Long.class);

        List<String> keys = items.stream().map(i -> "stock:" + i.getId()).toList();
        List<String> args = items.stream().map(i -> String.valueOf(i.getQuantity())).toList();
        Long result = redisTemplate.execute(redisScript, keys, args.toArray());

        if (result == 0L) {
            for (OrderItem item : items) {
                String key = "stock:" + item.getId();
                if (!redisTemplate.hasKey(key)) {
                    Long stockFromDb = inventoryRepository.getStock(item.getId());
                    redisTemplate.opsForValue().set(key, String.valueOf(stockFromDb), 10, TimeUnit.MINUTES);
                    System.out.println(redisTemplate.opsForValue().get("stock:2"));
                }
            }
            result = redisTemplate.execute(redisScript, keys, args.toArray());
        }

        if (result == 1L) {
            InventoryReservedEvent event = new InventoryReservedEvent(items);
            OutboxEvent outbox = new OutboxEvent("inventory_reserved", event.toJson());
            outboxEventRepository.save(outbox);
            return true;
        }
        return false;
    }

    public void updateInventoryFromRedis(String message) {
        CreateOrderEvent event = objectMapper.convertValue(message, CreateOrderEvent.class);


    }
}
