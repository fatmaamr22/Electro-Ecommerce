package com.example.electro.repository;

import com.example.electro.model.OrderItem;
import com.example.electro.model.OrderItemID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDAO extends JpaRepository<OrderItem, OrderItemID> {}
