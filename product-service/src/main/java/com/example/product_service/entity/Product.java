package com.example.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "import_price")
    Long importPrice;

    @Column(name = "sell_price")
    Long sellPrice;

    @Column(name = "description")
    String description;

    @Column(name = "owner_id")
    Long ownerId;

    @Column(name = "status")
    String status;
}
