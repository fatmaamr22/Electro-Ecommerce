package com.example.electro.controller;

import com.example.electro.enums.OrderState;
import com.example.electro.service.OrderService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
public class PaymentController {

    private final OrderService orderService;

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public void handlePaymentCallback(@RequestBody String Payload){
        JsonObject jsonObject = JsonParser.parseString(Payload).getAsJsonObject();
        boolean state = jsonObject.getAsJsonObject("obj").getAsJsonPrimitive("success").getAsBoolean();

        Integer orderId = Integer.parseInt(jsonObject.getAsJsonObject("obj")
                .getAsJsonObject("payment_key_claims")
                .getAsJsonObject("billing_data")
                .getAsJsonPrimitive("phone_number")
                .getAsString());

        if(state){
            orderService.updateOrderState(orderId, OrderState.PROCESSING);
        } else {
            orderService.updateOrderState(orderId, OrderState.CANCELLED);
        }
    }

}
