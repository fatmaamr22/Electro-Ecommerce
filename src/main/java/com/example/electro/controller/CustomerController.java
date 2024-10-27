package com.example.electro.controller;

import com.example.electro.customDetails.CustomAdminDetails;
import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.dto.AuthRequest;
import com.example.electro.model.Customer;
import com.example.electro.service.CustomerService;
import com.example.electro.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody Customer customer) {
        System.out.println("Received request to add new user: " + customer);
        System.out.println(customer.getCart());
         customerService.addCustomer(customer);
         return "User added successfully";
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        // Get the username from the Security Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Fetch the user profile using the username or userId
        Optional<Customer> customer = customerService.findByEmail(userDetails.getUsername()); // Assume this method exists in UserService

        if (customer != null) {
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("userId", customer.get().getId());
            profileData.put("firstName", customer.get().getFirstName());
            profileData.put("lastName", customer.get().getLastName());
            profileData.put("email", customer.get().getEmail());
            profileData.put("address", customer.get().getAddress());
            profileData.put("phone", customer.get().getPhone());
            profileData.put("dateOfBirth", customer.get().getDateOfBirth());
            profileData.put("job", customer.get().getJob());
            profileData.put("interests", customer.get().getInterests());

            return ResponseEntity.ok(profileData);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

}
