package com.example.electro.controller;

import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.dto.ErrorResponse;
import com.example.electro.dto.OrderDTO;
import com.example.electro.dto.payment.PaymentDTO;
import com.example.electro.model.Customer;
import com.example.electro.model.Order;
import com.example.electro.service.CustomerService;
import com.example.electro.service.OrderService;
import com.example.electro.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("order")
public class OrderCustomerController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final PaymentService paymentService;

    @Autowired
    public OrderCustomerController(OrderService orderService, CustomerService customerService, PaymentService paymentService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.paymentService = paymentService;
    }

    // Helper method to get the customerId from the authenticated user
    private int getAuthenticatedCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Assuming the customerId is stored in the username
        return userDetails.getId();
    }

    // Helper method to check if the user is authenticated
    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String);
    }

    @PostMapping
    public String placeOrder(HttpSession session, @RequestParam(value = "coupon", required = false) String coupon, Model model) {
        int customerId = getAuthenticatedCustomerId();
        if(coupon == null) {
            coupon = "";
        }

        if (isUserAuthenticated()){
            try {
                Customer customer = customerService.findCustomerById(customerId);
                Order order = orderService.addOrder(customer, coupon);
                if(order != null) {
                    // PAYMENT
                    System.out.println("payment inshallah");
                    PaymentDTO paymentDTO = new PaymentDTO(customer, order);
                    String paymentLink = paymentService.generatePaymentLink(paymentDTO);
                    return "redirect:" + paymentLink;
                } else{
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage("No Enough Stock");
                    model.addAttribute("errorResponse", errorResponse);
                    return "redirect:/cart";
                }
            } catch (RuntimeException e) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMessage(e.getMessage());
                model.addAttribute("errorResponse", errorResponse);
                return "redirect:/cart";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "login";
        }
    }

}
