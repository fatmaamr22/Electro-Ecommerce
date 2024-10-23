package com.example.electro.service;

import com.example.electro.dto.ProductDTO;
import com.example.electro.model.Product;
import com.example.electro.repository.ProductRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product findById(Integer id){
        return productRepository.findById(id).orElseThrow(()->new RuntimeException("Product Not Found"));
    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }
}
