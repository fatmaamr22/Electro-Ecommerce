package com.example.electro.controller.mvc;

import com.example.electro.enums.OrderState;
import com.example.electro.service.CartService;
import com.example.electro.service.OrderService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("payment")
public class PaymentController {

    private final OrderService orderService;
    private final CartService cartService;

    public PaymentController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping
    public String handlePaymentCallback(@RequestBody String Payload, Model model) {
        JsonObject jsonObject = JsonParser.parseString(Payload).getAsJsonObject();
        boolean state = jsonObject.getAsJsonObject("obj").getAsJsonPrimitive("success").getAsBoolean();

        Integer orderId = Integer.parseInt(jsonObject.getAsJsonObject("obj")
                .getAsJsonObject("payment_key_claims")
                .getAsJsonObject("billing_data")
                .getAsJsonPrimitive("phone_number")
                .getAsString());

        if(state){
            orderService.updateOrderState(OrderState.PROCESSING, orderId);
            orderService.updateProductStock(orderId);
            cartService.emptyCart(orderService.getOrderById(orderId).getCustomer().getId());
            return "index";
        } else {
            orderService.updateOrderState(OrderState.CANCELLED, orderId);
            model.addAttribute("message", "Payment Failed");
            model.addAttribute("status", 503);
            return "error";
        }
    }
}