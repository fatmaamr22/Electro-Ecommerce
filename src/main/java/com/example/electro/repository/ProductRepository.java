package com.example.electro.repository;

import com.example.electro.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {

    Optional<Product> findById(Integer integer);

    @Query("SELECT p FROM Product p WHERE p.stock > 1 AND p.deleted = false ORDER BY p.id DESC")
    List<Product> findLatestProducts(Pageable pageable);
}
