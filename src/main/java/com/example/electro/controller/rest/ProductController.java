package com.example.electro.controller.rest;


import com.example.electro.dto.ProductDTO;
import com.example.electro.dto.ProductRequestDTO;
import com.example.electro.dto.ProductWithSpecsDTO;
import com.example.electro.model.Product;
import com.example.electro.service.ProductService;
import com.example.electro.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<ProductDTO>> filterProducts(
                @ModelAttribute ProductRequestDTO productRequestDTO
    ) {




        Page<ProductDTO> filteredProducts = productService.findAllWithSpecifications(productRequestDTO);

        return ResponseEntity.ok(filteredProducts);

    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductWithSpecsDTO> findProductById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.findById(id));
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductWithSpecsDTO> updateProduct(@PathVariable int id,
                                                             @RequestBody ProductWithSpecsDTO productWithSpecsDTO
    ) {

        ProductWithSpecsDTO updatedProduct = productService.updateProduct(id, productWithSpecsDTO);
        productWithSpecsDTO.getImageURLs().stream().forEach(System.out::println);
        return ResponseEntity.ok(updatedProduct);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ProductWithSpecsDTO> createProduct(@RequestBody ProductWithSpecsDTO productWithSpecsDTO){
        return ResponseEntity.ok(productService.createProduct(productWithSpecsDTO));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable int id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
