package com.example.electro.repository;

import com.example.electro.model.Order;
import com.example.electro.model.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public class CustomOrderRepoImpl implements CustomOrderRepo{
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Order saveOrder(Order order, Set<OrderItem> orderItems) {
        em.persist(order);
        for (OrderItem orderItem : orderItems) {
            em.persist(orderItem);
        }
        return order;
    }
}
