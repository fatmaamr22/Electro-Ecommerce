package com.example.electro.controller;

import com.example.electro.dto.CartItemDTO;
import com.example.electro.service.CartService;
import com.example.electro.service.TemporaryCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final TemporaryCartService temporaryCartService;

    @Autowired
    public CartController(CartService cartService, TemporaryCartService temporaryCartService) {
        this.cartService = cartService;
        this.temporaryCartService = temporaryCartService;
    }

    // Helper method to check if the user is authenticated
    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String);
    }

    // Helper method to get the customerId from the authenticated user
    private int getAuthenticatedCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Assuming the customerId is stored in the username or you can map it from a custom UserDetails service
        return Integer.parseInt(userDetails.getUsername());
    }

    // Get all cart items for authenticated user or guest session cart
    @GetMapping
    public List<CartItemDTO> getCartItems(HttpSession session) {
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            return cartService.getCartItems(customerId);
        } else {
            return temporaryCartService.getCartItems(session);
        }
    }

    // Add an item to the cart (authenticated or guest)
    @PostMapping("/add/{productId}")
    public void addCartItem(@PathVariable int productId, HttpSession session) {
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            cartService.addCartItem(customerId, productId);
        } else {
            temporaryCartService.addCartItem(session, productId);
        }
    }

    // Remove an item from the cart (authenticated or guest)
    @DeleteMapping("/remove/{productId}")
    public void removeCartItem(@PathVariable int productId, HttpSession session) {
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            cartService.removeCartItem(customerId, productId);
        } else {
            temporaryCartService.removeCartItem(session, productId);
        }
    }

    // Set the quantity of an item in the cart (authenticated or guest)
    @PutMapping("/set/{productId}/{quantity}")
    public void setCartItemQuantity(@PathVariable int productId, @PathVariable int quantity, HttpSession session) {
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            cartService.setCartItemQuantity(customerId, productId, quantity);
        } else {
            temporaryCartService.setCartItemQuantity(session, productId, quantity);
        }
    }

}

