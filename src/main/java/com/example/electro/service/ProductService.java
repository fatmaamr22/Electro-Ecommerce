package com.example.electro.service;

import com.example.electro.dto.CategoryDTO;
import com.example.electro.dto.ProductDTO;
import com.example.electro.dto.ProductSpecsDTO;
import com.example.electro.dto.ProductWithSpecsDTO;
import com.example.electro.mapper.*;
import com.example.electro.model.Image;
import com.example.electro.model.Product;
import com.example.electro.model.ProductSpecs;
import com.example.electro.repository.ImagesRepository;
import com.example.electro.repository.ProductRepository;

import com.example.electro.repository.ProductSpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProductService {

    ProductRepository productRepository;
    ProductSpecsRepository productSpecsRepository;
    ImagesRepository imagesRepository;
    @Autowired
    public ProductService(ProductRepository productRepository , ProductSpecsRepository productSpecsRepository, ImagesRepository imagesRepository){
        this.productRepository = productRepository;
        this.productSpecsRepository = productSpecsRepository;
        this.imagesRepository = imagesRepository;
    }

    public ProductWithSpecsDTO findById(Integer id){
        return ProductWithSpecsMapper.INSTANCE.toDTO(productRepository.findById(id).get());
    }
    public Page<ProductDTO> findAll(Specification<Product> spec, Pageable pageable) {
        return productRepository.findAll(spec, pageable)
                .map(ProductMapper.INSTANCE::toDTO);
    }
    public List<ProductDTO> findLatestProducts(Pageable pageable){
        return ProductMapper.INSTANCE.toDTOs(productRepository.findLatestProducts(pageable));
    }
    public ProductWithSpecsDTO updateProduct(Integer id, ProductWithSpecsDTO productWithSpecsDTO) {
        // Retrieve the existing Product from the database
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        // Update specs
        ProductSpecs existingSpecs = existingProduct.getSpecs(); // Get existing specs
        if (existingSpecs == null) {
            // Handle case where specs do not exist, or create a new specs object if necessary
            existingSpecs = new ProductSpecs();
            existingProduct.setSpecs(existingSpecs);
        }
        // Map new values to existing specs
        existingSpecs = ProductSpecsMapper.INSTANCE.toEntity(productWithSpecsDTO.getSpecs());
        productSpecsRepository.save(existingSpecs); // Save updated specs

        List<Image> existingImages = existingProduct.getImages();

        for (int i = 0 ; i < existingImages.size() && i < productWithSpecsDTO.getImages().size(); i++){
            existingImages.get(i).setUrl(productWithSpecsDTO.getImages().get(i).getUrl());
        }
        //imagesRepository.saveAll(existingImages);
        existingProduct.setName(productWithSpecsDTO.getName());
        existingProduct.setDescription(productWithSpecsDTO.getDescription());
        existingProduct.setPrice(productWithSpecsDTO.getPrice());
        existingProduct.setImage(productWithSpecsDTO.getImage());
        existingProduct.setCategory(CategoryMapper.Instance.toEntity(productWithSpecsDTO.getCategory()));
        existingProduct.setBrandName(productWithSpecsDTO.getBrandName());
        existingProduct.setStock(productWithSpecsDTO.getStock());
        // Save the updated product

        Product updatedProduct = productRepository.save(existingProduct);

        //imagesRepository.saveAll(existingImages);

        // Convert and return the updated Product to DTO
        return ProductWithSpecsMapper.INSTANCE.toDTO(updatedProduct);
    }


}