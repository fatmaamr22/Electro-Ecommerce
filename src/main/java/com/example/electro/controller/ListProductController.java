package com.example.electro.controller;

import com.example.electro.dto.ProductDTO;
import com.example.electro.model.Product;
import com.example.electro.service.ProductService;
import com.example.electro.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("dashboard/products")
public class ListProductController {

    ProductService productService;

    @Autowired
    public ListProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                                 @RequestParam(value = "search_input",required = false) String searchInput,
                                 Model model) {
        int page = (pageNumber > 0) ? pageNumber - 1 : 0;

        Specification<Product> productSpec = ProductSpecification.withFilters(searchInput,null, null, null,null, null, null, null,null);
        Page<ProductDTO> productDTOPage = productService.findAllWithSpecifications(productSpec,PageRequest.of(page, 10));

        System.out.println(productDTOPage.getTotalElements());
        model.addAttribute("products", productDTOPage.getContent());
        model.addAttribute("page", pageNumber);
        model.addAttribute("totalPages",productDTOPage.getTotalPages());
        return "dashboard/list-product";
    }

}
