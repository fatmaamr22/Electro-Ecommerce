package com.example.electro.repository;

import com.example.electro.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDAO extends JpaRepository<Customer, Integer> {

    Customer findCustomerByEmail(String email);
    Customer findCustomerByPhone(String phone);
    Long countAll();

}
