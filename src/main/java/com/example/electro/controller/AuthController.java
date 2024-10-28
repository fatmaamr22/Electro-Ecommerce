package com.example.electro.controller;

import com.example.electro.customDetails.CustomAdminDetails;
import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.provider.CustomUserDetailsService;
import com.example.electro.service.JwtService;
import com.example.electro.service.TemporaryCartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    TemporaryCartService temporaryCartService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String welcome() {
        return "auth/login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String email, @RequestParam String password,
                              HttpServletRequest request, HttpServletResponse response,
                              RedirectAttributes redirectAttributes, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

                String token = jwtService.createToken(new HashMap<>(), email, isAdmin ? "ROLE_ADMIN" : "ROLE_USER");

                Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setSecure(true);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(60 * 60 * 10);

                response.addCookie(jwtCookie);

                if (isAdmin) {
                    modelAndView.setViewName("redirect:/dashboard/customers");
                } else {
                    temporaryCartService.mergeCarts(session, ((CustomUserDetails) userDetails).getId());
                    modelAndView.setViewName("redirect:/");
                }
            } else {
                redirectAttributes.addFlashAttribute("loginErrorResponse", "Invalid email or password");
                modelAndView.setViewName("redirect:/auth/login");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("loginErrorResponse", "Authentication failed. Please try again.");
            modelAndView.setViewName("redirect:/auth/login");
        }
        return modelAndView;
    }


    public void callGetEndpoint() {
        String url = "http://localhost:8080/cart";
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, null, Void.class);
        // Check response status if needed
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("cart merge request was successful.");
        } else {
            System.out.println("cart merge request failed.");
        }
    }


    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomAdminDetails) {
            CustomAdminDetails adminDetails = (CustomAdminDetails) principal;
            System.out.println("Admin ID: " + adminDetails.getId());
        } else if (principal instanceof CustomUserDetails) {
            CustomUserDetails customerDetails = (CustomUserDetails) principal;
            System.out.println("Customer ID: " + customerDetails.getId());
        } else if (principal instanceof String) {
            System.out.println("Principal is only a username: " + principal);
        } else {
            System.out.println("Unknown principal type: " + principal.getClass().getName());
        }

        return !(principal instanceof String);
    }


    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Create a cookie with the same name as the JWT token and set maxAge to 0
        Cookie jwtCookie = new Cookie("JWT_TOKEN", null); // Set the value to null
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/"); // Ensure the path is the same as when the cookie was created
        jwtCookie.setMaxAge(0); // Setting maxAge to 0 will delete the cookie

        // Add the cookie to the response to delete it
        response.addCookie(jwtCookie);

        // Redirect to the login page or home page after logging out
        return "redirect:/auth/login.jsp";
    }
}
