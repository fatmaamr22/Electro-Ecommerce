package com.example.electro.controller;

import com.example.electro.dto.OrderDTO;
import com.example.electro.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderCustomerController {
    private OrderService orderService;

    @Autowired
    public OrderCustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Review Order History Of Customer
    @GetMapping("/customers/{id}")
    public List<OrderDTO> getOrdersByCustomerId(@PathVariable("id") int customerId, @RequestParam(value = "page", defaultValue = "1") Integer pageNumber) {
        return orderService.getOrdersbyCustomerId(customerId, pageNumber,10);
    }


}
