package com.example.electro.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private int price;
    private String description;
    private int stock;
    private String image;
    private String brandName;
    private Boolean deleted;
}
