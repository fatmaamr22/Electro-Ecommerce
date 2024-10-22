package com.example.electro.repository;

import com.example.electro.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDAO extends JpaRepository<Customer, Integer> {

    Customer findByEmail(String email);
    Customer findByPhone(String phone);
    Long countAll();

}
