package com.example.electro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private List<ImageDTO> images;
    private List<String> imageURLs;
}

