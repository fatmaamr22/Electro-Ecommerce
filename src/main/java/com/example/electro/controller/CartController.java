package com.example.electro.controller;

import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.dto.CartItemDTO;
import com.example.electro.service.CartService;
import com.example.electro.service.TemporaryCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Assuming the customerId is stored in the username
        return userDetails.getId();
    }

    // Get all cart items for authenticated user or guest session cart
    @GetMapping
    public String getCartItemsToPage(Model model, HttpSession session) {
        List<CartItemDTO> cartProducts = new ArrayList<>();
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            cartProducts = cartService.getCartItems(customerId);
        } else {
            cartProducts = temporaryCartService.getCartItems(session);
        }
        model.addAttribute("cartItems", cartProducts);
        return "cart";
    }

    @GetMapping("/products")
    public List<CartItemDTO> getCartItems(Model model, HttpSession session) {
        List<CartItemDTO> cartProducts = new ArrayList<>();
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            cartProducts = cartService.getCartItems(customerId);
        } else {
            cartProducts = temporaryCartService.getCartItems(session);
        }
        model.addAttribute("cartItems", cartProducts);
        return cartProducts;
    }

    // Merges the guest cart into the persisted authenticated user's cart, must be invoked after the authenticated user's id already in the security context
    // To-Dd: test with Moamen
    @PostMapping
    public void mergeCarts(HttpSession session){
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            temporaryCartService.mergeCarts(session, customerId);
        } else {
        }
    }

    // Add an item to the cart (authenticated or guest)
    @PostMapping("/products/{productId}")
    public void addCartItem(@PathVariable int productId, HttpSession session) {
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            cartService.addCartItem(customerId, productId);
        } else {
            temporaryCartService.addCartItem(session, productId);
        }
    }

    // Add an item to the cart with a specified quantity (authenticated or guest)
    // To-Do: test with Mandour in the single product page
    @PostMapping("/products/{productId}/{quantity}")
    public ResponseEntity<Map<String, Integer>> addCartItemWithQuantity(@PathVariable int productId, @PathVariable int quantity, HttpSession session) {

        boolean added;
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            added = cartService.addCartItemWithQuantity(customerId, productId, quantity);
        } else {
            added = temporaryCartService.addCartItemWithQuantity(session, productId, quantity);
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("succeeded", added ? 1 : -1);
        return ResponseEntity.ok(response);
    }

    // Remove an item from the cart (authenticated or guest)
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable("productId") int productId, HttpSession session) {
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            cartService.removeCartItem(customerId, productId);
        } else {
            temporaryCartService.removeCartItem(session, productId); // Remove from the guest user's session-based cart
        }
        return ResponseEntity.ok().build();
    }


    // Set the quantity of an item in the cart (authenticated or guest)
    @PutMapping("/products/{productId}/{quantity}")
    public Map<String, Integer> updateQuantity(@PathVariable int productId, @PathVariable int quantity, HttpSession session) {
        boolean updated;
        CartItemDTO updatedItem;
        if (isUserAuthenticated()) {
            int customerId = getAuthenticatedCustomerId();
            updated = cartService.setCartItemQuantity(customerId, productId, quantity);
            updatedItem = cartService.getItem(customerId , productId);
        } else {
            updated = temporaryCartService.setCartItemQuantity(session, productId, quantity);
            updatedItem = temporaryCartService.getItem(session, productId);
        }

        int newTotal = updatedItem.getPrice() * updatedItem.getQuantity();
        Map<String, Integer> response = new HashMap<>();
        response.put("newTotal", updated ? newTotal : -1);
        return response;
    }

}
