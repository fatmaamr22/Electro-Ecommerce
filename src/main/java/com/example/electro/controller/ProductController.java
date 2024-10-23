package com.example.electro.controller;


import com.example.electro.dto.ProductDTO;
import com.example.electro.model.Product;
import com.example.electro.service.ProductService;
import com.example.electro.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> filterProducts(
            @RequestParam(value = "category", required = false) List<Integer> categories,
            @RequestParam(value = "brand", required = false) List<String> brands,
            @RequestParam(value = "processor", required = false) List<String> processors,
            @RequestParam(value = "memory", required = false) List<Integer> memoryOptions,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            Model model
    ) {
        Specification<Product> productSpec = ProductSpecification.withFilters(categories, brands, processors, memoryOptions, minPrice, maxPrice);

        List<ProductDTO> filteredProducts = productService.findAll(productSpec);

        model.addAttribute("products", filteredProducts);

        return filteredProducts;
    }
    @GetMapping("/{id}")
    public ProductDTO findProductById(@PathVariable Integer id){
        return productService.findById(id);
    }
}
