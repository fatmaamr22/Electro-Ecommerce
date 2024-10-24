package com.example.electro.controller;

import com.example.electro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard/products")
public class ProductAdminController {

    ProductService productService;

    @Autowired
    public ProductAdminController(ProductService productService){
        this.productService = productService;
    }



}
