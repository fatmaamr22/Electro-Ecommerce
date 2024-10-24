package com.example.electro.controller;

import com.example.electro.dto.ProductDTO;
import com.example.electro.model.Product;
import com.example.electro.service.FilterService;
import com.example.electro.service.ProductService;
import com.example.electro.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("shop")
public class ShopController {

    FilterService filterService;

    @Autowired
    public ShopController(ProductService productService, FilterService filterService, DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration){
        this.filterService = filterService;
    }
    @GetMapping
    public String loadShop(Model model){
        model.addAttribute("brandList", filterService.getBrands());
        model.addAttribute("processorList", filterService.getProcessors());
        model.addAttribute("memoryList", filterService.getMemoryList());
        model.addAttribute("osList", filterService.getOsList());
        model.addAttribute("batteryList", filterService.getBatteryList());
        model.addAttribute("categoryList", filterService.getCategories());
        model.addAttribute("minPrice", filterService.getMinPrice());
        model.addAttribute("maxPrice", filterService.getMaxPrice());

        return "category";
    }

}
