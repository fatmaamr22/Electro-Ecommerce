package com.example.electro.service;

import com.example.electro.model.Coupon;
import com.example.electro.model.Order;
import com.example.electro.repository.CouponDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CouponService {
    private CouponDAO couponDAO;

    public CouponService(CouponDAO couponDAO) {
        this.couponDAO = couponDAO;
    }

    public Coupon addCoupon(Coupon coupon) {
        return couponDAO.save(coupon);
    }

    public void deleteCoupon(Coupon coupon) {
        couponDAO.delete(coupon);
    }

    public Coupon updateCoupon(Coupon coupon) {
        return couponDAO.save(coupon);
    }

    public Optional<Coupon> findCouponById(int id) {
        Optional<Coupon> c = couponDAO.findById(id);
        return c;
    }

    public List<Coupon> findAllCoupons() {
        return couponDAO.findAll();
    }

    public Coupon findCouponByName(String name) {
        Coupon c = couponDAO.findCouponByCoupon(name);
        return c;
    }

    public List<Integer> getCouponInfo(String couponName){
        Coupon coupon = couponDAO.findCouponByCoupon(couponName);
        List<Integer> info = new ArrayList<>();
        info.add(coupon.getPercentage());
        info.add(coupon.getLimitPayment());
        return info;
    }

    public boolean validateCoupon(String couponName){
        Coupon coupon = couponDAO.findCouponByCoupon(couponName);
        if (coupon != null){
            if(coupon.getEndDate().before(new Date())) {
                return false;
            }
            return true;
        }
        return false;
    }
}
