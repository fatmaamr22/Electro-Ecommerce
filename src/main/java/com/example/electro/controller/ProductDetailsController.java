package com.example.electro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("product-details")
public class ProductDetailsController {

    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable("id") Long productId, Model model) {

        model.addAttribute("productId", productId);

        return "single-product";
    }
}
