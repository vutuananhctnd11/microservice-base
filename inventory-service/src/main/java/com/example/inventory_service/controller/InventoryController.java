package com.example.inventory_service.controller;

import com.example.inventory_service.dto.ApiResponse;
import com.example.inventory_service.dto.InventoryReservedEvent;
import com.example.inventory_service.dto.OrderItem;
import com.example.inventory_service.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryController {

    InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> reserve (@RequestBody List<OrderItem> items) {
        ApiResponse<Boolean> response = new ApiResponse<>(inventoryService.checkAndReserve(items));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
