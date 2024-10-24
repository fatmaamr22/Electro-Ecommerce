package com.example.electro.service;

import com.example.electro.model.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final Customer customer;

    public CustomUserDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // All customers will have the "ROLE_USER"
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return customer.getPassword(); // Return the hashed password
    }

    @Override
    public String getUsername() {
        return customer.getEmail(); // Use email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement your logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement your logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement your logic if needed
    }

    @Override
    public boolean isEnabled() {
        return customer.isActive(); // Assuming you have an active flag in Customer
    }
}
