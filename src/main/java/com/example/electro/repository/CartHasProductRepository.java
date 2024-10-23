package com.example.electro.repository;

import com.example.electro.model.CartHasProduct;
import com.example.electro.model.CartHasProductID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartHasProductRepository extends JpaRepository<CartHasProduct, CartHasProductID> {
    List<CartHasProduct> findByCartId(Integer cartId);
}