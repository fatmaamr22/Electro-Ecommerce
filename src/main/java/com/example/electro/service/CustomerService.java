package com.example.electro.service;
import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.dto.CustomerDTO;
import com.example.electro.dto.OrderDTO;
import com.example.electro.mapper.CustomerMapper;
import com.example.electro.mapper.OrderMapper;
import com.example.electro.model.Customer;
import com.example.electro.model.Order;
import com.example.electro.repository.CartRepository;
import com.example.electro.repository.CustomerRepository;
import com.example.electro.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {
    private CustomerRepository customerRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
}


@Transactional
public Customer addCustomer(Customer customer) {
    cartRepository.save(customer.getCart());
    wishlistRepository.save(customer.getWishlist());
    return customerRepository.save(customer);
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

    public CustomerDTO findById(Integer id) {
    return CustomerMapper.INSTANCE.toDTO(customerRepository.findById(id).get());
}

    public List<CustomerDTO> getAllCustomers(int pageNo, int size) {
        int page = (pageNo > 0) ? pageNo - 1 : 0;
        Page<Customer> customers = customerRepository.findAll(PageRequest.of(page, size));
        return CustomerMapper.INSTANCE.toDTOs(customers.getContent());
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

    @Override
    public UserDetails loadUserByUsername(String email) {
        // Find customer by email
        System.out.println("Inside Customer SERVICE loadUserByUsername");
        return customerRepository.findCustomerByEmail(email)
                .map(CustomUserDetails::new) // Return CustomUserDetails if found
                .orElse(null); // Return null if not found
    }


}
