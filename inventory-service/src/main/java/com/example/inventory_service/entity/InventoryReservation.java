package com.example.inventory_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "inventory_reservation")
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column(name = "orrder_id")
    Long orderId;

    @Column(name = "product_id")
    Long productId;

    @Column(name = "quantity")
    Long quantity;

    @Column(name = "status")
    String status;

    @Column(name = "reserved_at")
    LocalDateTime reservedAt;
}
