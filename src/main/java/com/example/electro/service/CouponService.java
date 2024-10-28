package com.example.electro.service;

import com.example.electro.model.Coupon;
import com.example.electro.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    private CouponRepository couponRepo;

    public CouponService(CouponRepository couponRepo) {
        this.couponRepo = couponRepo;
    }

    public Coupon addCoupon(Coupon coupon) {
        return couponRepo.save(coupon);
    }

    public void deleteCoupon(Coupon coupon) {
        couponRepo.delete(coupon);
    }

    public Coupon updateCoupon(Coupon coupon) {
        return couponRepo.save(coupon);
    }

    public Optional<Coupon> findCouponById(int id) {
        Optional<Coupon> c = couponRepo.findById(id);
        return c;
    }

    public List<Coupon> findAllCoupons() {
        return couponRepo.findAll();
    }

    public Coupon findCouponByName(String name) {
        Coupon c = couponRepo.findCouponByCoupon(name);
        return c;
    }

    public List<Integer> getCouponInfo(String couponName){
        Coupon coupon = couponRepo.findCouponByCoupon(couponName);
        List<Integer> info = new ArrayList<>();
        info.add(coupon.getPercentage());
        info.add(coupon.getLimitPayment());
        return info;
    }

    public boolean validateCoupon(String couponName){
        Coupon coupon = couponRepo.findCouponByCoupon(couponName);
        if (coupon != null){
            if(coupon.getEndDate().before(new Date())) {
                return false;
            }
            return true;
        }
        return false;
    }
}
