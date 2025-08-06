package com.example.product_service.repository;

import com.example.product_service.entity.Product;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByOrderByNameAsc (Pageable pageable);

    @Query ("""
        SELECT p
        FROM Product p
        WHERE p.id IN :ids
    """)
    List<Product> getListProductsById(List<Long> ids);
}
