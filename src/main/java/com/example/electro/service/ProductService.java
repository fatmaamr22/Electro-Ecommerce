package com.example.electro.service;

import com.example.electro.dto.ProductDTO;
import com.example.electro.dto.ProductRequestDTO;
import com.example.electro.dto.ProductWithSpecsDTO;
import com.example.electro.mapper.*;
import com.example.electro.model.Image;
import com.example.electro.model.Product;
import com.example.electro.model.ProductSpecs;
import com.example.electro.repository.ImagesRepository;
import com.example.electro.repository.ProductRepository;
import com.example.electro.repository.ProductSpecsRepository;
import com.example.electro.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    public Page<ProductDTO> findAllWithSpecifications(ProductRequestDTO productRequestDTO) {
        Specification<Product> productSpec = ProductSpecification.builder()
                .withBrands(productRequestDTO.brands())
                .withDeleted(false)
                .withCategories(productRequestDTO.categories())
                .withProcessors(productRequestDTO.processors())
                .withMemoryOptions(productRequestDTO.memoryOptions())
                .withPriceRange(productRequestDTO.minPrice(), productRequestDTO.maxPrice())
                .withOperatingSystem(productRequestDTO.operatingSystem())
                .withSearchInput(productRequestDTO.searchInput())
                .build();
        int pageNo = (productRequestDTO.page() > 0) ? productRequestDTO.page() - 1 : 0;

        return productRepository.findAll(productSpec, PageRequest.of(pageNo, productRequestDTO.size()))
                .map(ProductMapper.INSTANCE::toDTO);
    }
    public Page<ProductDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper.INSTANCE::toDTO);
    }
    public List<ProductDTO> findLatestProducts(Pageable pageable){
        return ProductMapper.INSTANCE.toDTOs(productRepository.findLatestProducts(pageable));
    }

    @Transactional
    public ProductWithSpecsDTO updateProduct(Integer id, ProductWithSpecsDTO productWithSpecsDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        ProductSpecs existingSpecs = existingProduct.getSpecs(); // Get existing specs
        if (existingSpecs == null) {
            existingSpecs = new ProductSpecs();
            existingProduct.setSpecs(existingSpecs);
        }
        existingSpecs = ProductSpecsMapper.INSTANCE.toEntity(productWithSpecsDTO.getSpecs());
        productSpecsRepository.save(existingSpecs); // Save updated specs

        List<Image> existingImages = existingProduct.getImages();

        // Update or add images based on the size of existing and new images
        for (int i = 0; i < productWithSpecsDTO.getImages().size(); i++) {
            if (i < existingImages.size()) {
                // Override existing images
                existingImages.get(i).setUrl(productWithSpecsDTO.getImages().get(i).getUrl());
            } else {
                // Add new images
                Image newImage = new Image();
                newImage.setUrl(productWithSpecsDTO.getImages().get(i).getUrl());
                newImage.setProduct(existingProduct);
                imagesRepository.save(newImage);
                existingImages.add(newImage);
            }

        }

        existingProduct.setName(productWithSpecsDTO.getName());
        existingProduct.setDescription(productWithSpecsDTO.getDescription());
        existingProduct.setPrice(productWithSpecsDTO.getPrice());
        existingProduct.setImage(productWithSpecsDTO.getImage());
        existingProduct.setCategory(CategoryMapper.Instance.toEntity(productWithSpecsDTO.getCategory()));
        existingProduct.setBrandName(productWithSpecsDTO.getBrandName());
        existingProduct.setStock(productWithSpecsDTO.getStock());

        Product updatedProduct = productRepository.save(existingProduct);

        return ProductWithSpecsMapper.INSTANCE.toDTO(updatedProduct);
    }

    public  void deleteProduct(int id){
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductWithSpecsDTO createProduct(ProductWithSpecsDTO productWithSpecsDTO){
        Product newProduct = ProductWithSpecsMapper.INSTANCE.toEntity(productWithSpecsDTO);
        for (Image image: newProduct.getImages()){
            image.setProduct(newProduct);
        }
        ProductWithSpecsDTO createdProductWithSpecsDto = ProductWithSpecsMapper.INSTANCE.toDTO(productRepository.save(newProduct));
        imagesRepository.saveAll(newProduct.getImages());
        return createdProductWithSpecsDto;
    }

}