package com.example.electro.controller;

import com.example.electro.dto.OrderDTO;
import com.example.electro.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("dashboard/orders")
public class OrderAdminController {
    private final OrderService orderService;

    public OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Review Specific Order in Admin Panel
    @GetMapping("/{id}")
    public String reviewOrder(@PathVariable("id") int id, Model model) {
        OrderDTO order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "dashboard/review-order";
    }

    // Review All orders in Admin Panel
    @GetMapping
    public String getAllOrders(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, Model model) {
        List<OrderDTO> orders = orderService.getAllOrders(pageNumber, 10);
        Long count = orderService.countOrders();
        System.out.println(count);
        model.addAttribute("orders", orders);
        model.addAttribute("page", pageNumber);
        model.addAttribute("totalPages", (count+10-1)/10);
        return "dashboard/list-orders";
    }


//    // Review All Orders Using A Specific Coupon in Admin Panel
//    @GetMapping("/coupons/{name}")
//    public List<OrderDTO> getOrdersByCoupon(@PathVariable("name") String couponName) {
//        return orderService.getOrdersbyCouponName(couponName);
//    }

}
