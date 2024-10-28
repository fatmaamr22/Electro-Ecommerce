package com.example.electro.controller;

import com.example.electro.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard/create-product")
public class CreateProductController {
    CategoryService categoryService;
    public CreateProductController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public String updateProductView( Model model) {
        model.addAttribute("categoryList",categoryService.findAll());
        return "dashboard/add-product";
    }
}
