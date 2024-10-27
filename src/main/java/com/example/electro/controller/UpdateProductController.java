package com.example.electro.controller;

import com.example.electro.dto.OrderDTO;
import com.example.electro.service.CategoryService;
import com.example.electro.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard/products")
public class UpdateProductController {

    ProductService productService;
    CategoryService categoryService;
    public UpdateProductController (ProductService productService,CategoryService categoryService){
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public String updateProductView(@PathVariable("id") int id,Model model) {
        model.addAttribute("product",productService.findById(id));
        model.addAttribute("categoryList",categoryService.findAll());
        return "dashboard/update-product";
    }
}
