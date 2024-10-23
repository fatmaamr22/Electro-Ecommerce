package com.example.electro.service;

import com.example.electro.dto.ProductDTO;
import com.example.electro.mapper.ProductMapper;
import com.example.electro.model.Product;
import com.example.electro.repository.ProductRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ProductDTO> findAll(Specification<Product> specification){
        return ProductMapper.INSTANCE.toDTOs(productRepository.findAll(specification));
    }
}
