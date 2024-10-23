package com.example.electro.repository;

import com.example.electro.model.Order;
import com.example.electro.model.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Repository
public abstract class OrderRepoImpl implements OrderRepository {
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
