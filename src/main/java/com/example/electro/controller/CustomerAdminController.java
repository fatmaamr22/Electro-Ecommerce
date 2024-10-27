package com.example.electro.controller;

import com.example.electro.dto.CustomerDTO;
import com.example.electro.dto.OrderDTO;
import com.example.electro.model.Customer;
import com.example.electro.service.CustomerService;
import com.example.electro.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("dashboard/customers")
public class CustomerAdminController {
    private final CustomerService customerService;

    public CustomerAdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Review Specific Order in Admin Panel
    @GetMapping("/{id}")
    public String reviewOrder(@PathVariable("id") int id, Model model) {
        CustomerDTO customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "dashboard/review-customer";
    }

    // Review All orders in Admin Panel
    @GetMapping
    public String getAllCustomers(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, Model model) {
        List<CustomerDTO> customers = customerService.getAllCustomers(pageNumber, 10);
        Long count = customerService.countAll();
        System.out.println(count);
        model.addAttribute("customers", customers);
        model.addAttribute("page", pageNumber);
        model.addAttribute("totalPages", (count+10-1)/10);
        return "dashboard/list-customer";
    }


//    // Review All Orders Using A Specific Coupon in Admin Panel
//    @GetMapping("/coupons/{name}")
//    public List<OrderDTO> getOrdersByCoupon(@PathVariable("name") String couponName) {
//        return orderService.getOrdersbyCouponName(couponName);
//    }

}
