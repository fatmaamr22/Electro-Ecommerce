package com.example.electro.repository;

import com.example.electro.enums.OrderState;
import com.example.electro.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer>, CustomOrderRepo{
    Page<Order> findAll(Pageable pageable);
//    Page<Order> findAllOrdersByCustomerId(int id, Pageable pageable);
    @Query(value = "select o from Order o where o.coupon.coupon = :couponName")
    List<Order> findOrdersByCoupon(@Param("couponName") String couponName);
    List<Order> findOrdersByCustomerId(Integer customerId);

    @Transactional
    @Modifying
    @Query("update Order o set o.state = :state where o.id = :orderId")
    void updateOrderState(@Param("state") OrderState state, @Param("orderId") int orderId);
}
