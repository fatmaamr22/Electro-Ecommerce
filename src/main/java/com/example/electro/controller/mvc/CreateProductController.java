package com.example.electro.controller.mvc;

import com.example.electro.service.FilterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard/create-product")
public class CreateProductController {
    FilterService filterService;
    public CreateProductController(FilterService filterService){
        this.filterService = filterService;
    }

    @GetMapping
    public String updateProductView( Model model) {
        model.addAttribute("brandList", filterService.getBrands());
        model.addAttribute("processorList", filterService.getProcessors());
        model.addAttribute("memoryList", filterService.getMemoryList());
        model.addAttribute("osList", filterService.getOsList());
        model.addAttribute("batteryList", filterService.getBatteryList());
        model.addAttribute("categoryList", filterService.getCategories());
        return "dashboard/add-product";
    }
}
