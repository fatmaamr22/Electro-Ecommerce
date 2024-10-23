package com.example.electro.controller;

import com.example.electro.dto.OrderDTO;
import com.example.electro.model.Order;
import com.example.electro.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Review Specific Order in Admin Panel
    @GetMapping("/{id}")
    public OrderDTO reviewOrder(@PathVariable("id") int id) {
        return orderService.getOrderById(id);
    }

    // Review All orders in Admin Panel
    @GetMapping
    public List<OrderDTO> getAllOrders(@RequestParam(value = "page") Integer pageNumber) {
        return orderService.getAllOrders(pageNumber,10);
    }

    // Review Order History Of Customer
    @GetMapping("/customers/{id}")
    public List<OrderDTO> getOrdersByCustomerId(@PathVariable("id") int customerId, @RequestParam(value = "page") Integer pageNumber) {
        return orderService.getOrdersbyCustomerId(customerId,pageNumber,10);
    }

    // Review All Orders Using A Specific Coupon in Admin Panel
    @GetMapping("/coupons/{name}")
    public List<OrderDTO> getOrdersByCoupon(@PathVariable("name") String couponName) {
        return orderService.getOrdersbyCouponName(couponName);
    }

}
