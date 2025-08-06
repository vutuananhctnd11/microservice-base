package com.example.order_service.scheduler;


import com.example.order_service.entity.OutboxEvent;
import com.example.order_service.repository.OutboxEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboxScheduler {

    OutboxEventRepository outboxEventRepository;
    KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    public void publicPendingEvents() {
        List<OutboxEvent> events = outboxEventRepository.findByStatus("PENDING");

        for (OutboxEvent event : events) {
            kafkaTemplate.send("inventory.update", event.getPayload());
            event.setStatus("SENT");
            outboxEventRepository.save(event);
            log.info("Sent event {} to Kafka", event.getPayload());
        }
    }
}
