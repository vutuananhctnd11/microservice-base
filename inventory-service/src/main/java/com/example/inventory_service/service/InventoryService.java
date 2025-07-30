package com.example.inventory_service.service;

import com.example.inventory_service.repository.InventoryRepository;
import com.example.inventory_service.repository.InventoryReservationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryService {

    InventoryRepository inventoryRepository;
    InventoryReservationRepository inventoryReservationRepository;


}
