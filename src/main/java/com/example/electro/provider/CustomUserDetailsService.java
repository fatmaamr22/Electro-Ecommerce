package com.example.electro.provider;

import com.example.electro.service.AdminService;
import com.example.electro.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerService customerService; // Inject CustomerService
    @Autowired
    private AdminService adminService; // Inject AdminService

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // If not found as Customer, check Admin
        UserDetails userDetails = adminService.loadUserByUsername(username);

        if (userDetails != null) {
            return userDetails; // User found as Admin
        }
         userDetails = customerService.loadUserByUsername(username);

        if (userDetails != null) {
            return userDetails; // User found as Customer
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
