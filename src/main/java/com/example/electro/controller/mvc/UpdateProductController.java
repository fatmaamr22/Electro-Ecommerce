package com.example.electro.controller.mvc;

import com.example.electro.service.FilterService;
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
    FilterService filterService;
    public UpdateProductController (ProductService productService,FilterService filterService){
        this.productService = productService;
        this.filterService = filterService;
    }

    @GetMapping("/{id}")
    public String updateProductView(@PathVariable("id") int id,Model model) {
        model.addAttribute("product",productService.findById(id));
        model.addAttribute("categoryList",filterService.getCategories());
        model.addAttribute("brandList", filterService.getBrands());
        model.addAttribute("processorList", filterService.getProcessors());
        model.addAttribute("memoryList", filterService.getMemoryList());
        model.addAttribute("osList", filterService.getOsList());
        model.addAttribute("batteryList", filterService.getBatteryList());
        return "dashboard/update-product";
    }
}
