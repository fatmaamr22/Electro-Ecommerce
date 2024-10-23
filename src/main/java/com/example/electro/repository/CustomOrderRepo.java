package com.example.electro.repository;

import com.example.electro.model.Order;
import com.example.electro.model.OrderItem;

import java.util.Set;

public interface CustomOrderRepo {
    Order saveOrder(Order order, Set<OrderItem> orderItems);
}
