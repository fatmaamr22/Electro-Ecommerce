package com.example.electro.dto;

import com.example.electro.mapper.CategoryMapper;
import com.example.electro.model.Image;
import com.example.electro.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private String image;
    private Set<ImageDTO> images;

}

