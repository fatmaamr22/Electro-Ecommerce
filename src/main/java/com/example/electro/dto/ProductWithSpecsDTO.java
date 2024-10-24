package com.example.electro.dto;

import com.example.electro.mapper.CategoryMapper;
import com.example.electro.model.Image;
import com.example.electro.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductWithSpecsDTO {
    private Integer id;
    private String name;
    private int price;
    private String description;
    private int stock;
    private String brandName;
    private ProductSpecsDTO specs;
    private CategoryDTO category;
    private List<String> images = new ArrayList<>();

    public ProductWithSpecsDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.stock = product.getStock();
        this.brandName = product.getBrandName();
        this.specs = new ProductSpecsDTO(product.getSpecs());
        this.category = CategoryMapper.Instance.toDTO(product.getCategory());
        images.add(product.getImage());
        for(Image image:product.getImages()){
            images.add(image.getUrl());
        }
    }
}

