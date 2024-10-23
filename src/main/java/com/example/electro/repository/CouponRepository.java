package com.example.electro.repository;

import com.example.electro.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Coupon findCouponByCoupon(String name);
    List<Coupon> findAll();
}
