package com.example.electro.repository;

import com.example.electro.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponDAO extends JpaRepository<Coupon, Integer> {

    Coupon findByName(String name);
}
