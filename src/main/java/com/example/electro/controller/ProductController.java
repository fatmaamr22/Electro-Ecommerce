package com.example.electro.controller;


import com.example.electro.dto.ProductDTO;
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
            @RequestParam(value = "category", required = false) List<Integer> categories,
            @RequestParam(value = "brand", required = false) List<String> brands,
            @RequestParam(value = "deleted", defaultValue = "false",required = false) Boolean deleted,
            @RequestParam(value = "processor", required = false) List<String> processors,
            @RequestParam(value = "memory", required = false) List<Integer> memoryOptions,
            @RequestParam(value = "min-price", required = false) Integer minPrice,
            @RequestParam(value = "max-price", required = false) Integer maxPrice,
            @RequestParam(value = "os", required = false) List<String> operatingSystem,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "12") int size
    ) {

        Specification<Product> productSpec = ProductSpecification.withFilters(null,categories, brands, processors,operatingSystem, memoryOptions, minPrice, maxPrice,deleted);
        int pageNo = (page > 0) ? page - 1 : 0;
        System.out.println("deleted"+deleted);
        Pageable pageable = PageRequest.of(pageNo, size);

        Page<ProductDTO> filteredProducts = productService.findAllWithSpecifications(productSpec, pageable);

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
