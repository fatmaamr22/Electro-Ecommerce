package com.example.electro.controller;

import com.example.electro.dto.ProductDTO;
import com.example.electro.repository.ProductRepository;
import com.example.electro.service.ProductService;
import org.springframework.data.domain.PageRequest;
import com.example.electro.dto.ProductDTO;
import com.example.electro.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    ProductService productService;

    public HomeController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/")
    public String home(Model model) {

        List<ProductDTO> latestProducts1 = productService.findLatestProducts(PageRequest.of(0, 8));
        List<ProductDTO> latestProducts2 = productService.findLatestProducts(PageRequest.of(1, 8));


        model.addAttribute("latestProducts",latestProducts1);
        model.addAttribute("latestProducts2",latestProducts2);
        return "index";
    }
}
