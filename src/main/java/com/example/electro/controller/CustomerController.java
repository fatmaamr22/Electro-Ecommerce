package com.example.electro.controller;

import com.example.electro.customDetails.CustomAdminDetails;
import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.dto.AuthRequest;
import com.example.electro.dto.CustomerDTO;
import com.example.electro.model.Customer;
import com.example.electro.model.TemporaryCart;
import com.example.electro.service.CustomerService;
import com.example.electro.service.JwtService;
import com.example.electro.service.TemporaryCartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TemporaryCartService temporaryCartService;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@ModelAttribute Customer customer, HttpSession session,
                             HttpServletResponse response) {
        // Hash the password
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        // Save the customer
        customerService.addCustomer(customer);

        // Generate the JWT token after user registration
        String token = jwtService.createToken(new HashMap<>(), customer.getEmail(), "ROLE_USER");

        // Create a cookie to store the token
        Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
        jwtCookie.setHttpOnly(true); // Prevent JavaScript from accessing the token
        jwtCookie.setSecure(true); // Ensure the cookie is only sent over HTTPS
        jwtCookie.setPath("/"); // Set the cookie path
        jwtCookie.setMaxAge(60 * 60 * 10); // Set expiry time for the token (10 hours)

        // Add the cookie to the response
        response.addCookie(jwtCookie);

        // Merge temporary cart with user's cart
        temporaryCartService.mergeCarts(session, customer.getId());

        return "redirect:/"; // Redirect to home page after registration
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("password") String password) {
        // Encrypt the new password
        String encodedPassword = passwordEncoder.encode(password);

        // Call the service method to update the password in the database
        boolean isUpdated = customerService.updateUserPassword(encodedPassword);

        if (isUpdated) {
            return "redirect:/profile.jsp";
        } else {
            return "redirect:/profile.jsp";
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmailAvailability(@RequestParam String email) {
        System.out.println("Check email availability");
        Optional<Customer> customer = customerService.findByEmail(email);

        if (customer.isPresent()) {
            return ResponseEntity.ok("false"); // Email exists, not available
        } else {
            return ResponseEntity.ok("true"); // Email does not exist, available
        }
    }

    @PostMapping("/update")
    public String updateCustomer(Long id, Customer updatedCustomer) {
        Optional<Customer> optionalCustomer = customerService.findByEmail(updatedCustomer.getEmail());
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            // Set values from the updated customer to the existing customer
            existingCustomer.setFirstName(updatedCustomer.getFirstName());
            existingCustomer.setLastName(updatedCustomer.getLastName());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setAddress(updatedCustomer.getAddress());
            existingCustomer.setPhone(updatedCustomer.getPhone());
            existingCustomer.setDateOfBirth(updatedCustomer.getDateOfBirth()); // Ensure this is set
            existingCustomer.setJob(updatedCustomer.getJob());
            existingCustomer.setInterests(updatedCustomer.getInterests());

            customerService.save(existingCustomer); // Save changes
        } else {
            throw new NoSuchElementException("Customer not found with ID: " + id);
        }
        return "redirect:/profile.jsp";
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
