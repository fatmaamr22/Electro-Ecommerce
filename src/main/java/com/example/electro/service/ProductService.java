package com.example.electro.service;

import com.example.electro.dto.ProductDTO;
import com.example.electro.mapper.ProductMapper;
import com.example.electro.model.Product;
import com.example.electro.repository.ProductRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductDTO findById(Integer id){
        return ProductMapper.INSTANCE.toDTO(productRepository.findById(id).orElseThrow(()->new RuntimeException("Product Not Found")));
    }
    public Page<ProductDTO> findAll(Specification<Product> spec, Pageable pageable) {
        return productRepository.findAll(spec, pageable)
                .map(ProductMapper.INSTANCE::toDTO);
    }
}
