package com.example.electro.service;
import com.example.electro.model.Customer;
import com.example.electro.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class CustomerService {
    private CustomerRepository customerRepository;

public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
}
public Long countAll() {
    return customerRepository.count();
}

public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

public Boolean checkEmailAvailability(String email) {
    return findByEmail(email).isEmpty();
}
public Optional<Customer> findByEmail(String email) {return customerRepository.findCustomerByEmail(email);}
public Optional<Customer> findById(Integer id) {
    return customerRepository.findById(id);
}

//Old method did not use id customer to update
public Customer updateCustomer(Integer id, Customer customerDetails) {
    Optional<Customer> optionalCustomer = customerRepository.findById(id);
    if (optionalCustomer.isPresent()) {
        Customer customerToUpdate = optionalCustomer.get();
        customerToUpdate.setFirstName(customerDetails.getFirstName());
        customerToUpdate.setLastName(customerDetails.getLastName());
        customerToUpdate.setEmail(customerDetails.getEmail());
        customerToUpdate.setPassword(customerDetails.getPassword()); //Handle Password security /hash
        customerToUpdate.setAddress(customerDetails.getAddress());
        customerToUpdate.setPhone(customerDetails.getPhone());
        customerToUpdate.setDateOfBirth(customerDetails.getDateOfBirth());
        customerToUpdate.setJob(customerDetails.getJob());
        customerToUpdate.setInterests(customerDetails.getInterests());
        return customerRepository.save(customerToUpdate);
    } else {
        throw new RuntimeException("Customer not found with id " + id);
    }
}
}
