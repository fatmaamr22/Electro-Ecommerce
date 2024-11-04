package com.example.electro.controller.mvc;

import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.dto.CartItemDTO;
import com.example.electro.dto.ErrorResponse;
import com.example.electro.service.CartService;
import com.example.electro.service.CouponService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("order-details")
public class ReviewOrderController {

    private final CartService cartService;
    private final CouponService couponService;

    public ReviewOrderController(CartService cartService, CouponService couponService) {
        this.cartService = cartService;
        this.couponService = couponService;
    }

    // Helper method to get the customerId from the authenticated user
    private int getAuthenticatedCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Assuming the customerId is stored in the username
        return userDetails.getId();
    }

    // Helper method to check if the user is authenticated
    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String);
    }

    @GetMapping
    public String showOrderDetails(Model model, HttpSession session, @RequestParam(value = "coupon", required = false) String coupon) {

        if(!isUserAuthenticated()) {
            return "auth/login";
        }

        int customerId = getAuthenticatedCustomerId();

        List<CartItemDTO> cartItems = cartService.getCartItems(customerId);
        int totalPrice = cartItems.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("coupon", coupon);

        if (coupon == null || coupon.isEmpty()) {
            return "checkout";
        } else if (!couponService.validateCoupon(coupon)) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Invalid coupon");
            session.setAttribute("errorResponse", errorResponse);
            return "redirect:/cart";
        }

        List<Integer> couponInfo = couponService.getCouponInfo(coupon);
        int discount = Math.min(totalPrice * couponInfo.get(0) / 100, couponInfo.get(1));
        int newTotalPrice = totalPrice - discount;

        model.addAttribute("newTotalPrice", newTotalPrice);
        model.addAttribute("discount", discount);
        return "checkout";
    }
}
