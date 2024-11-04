package com.example.electro.controller.rest;

import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.dto.OrderDTO;
import com.example.electro.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private int getAuthenticatedCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    // Review Order History Of Customer
    @GetMapping("/customer")
    public List<OrderDTO> getOrdersByCustomerId() {
        int customerId = getAuthenticatedCustomerId();
        return orderService.getOrdersbyCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> reviewOrder(@PathVariable("id") int id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }
}
