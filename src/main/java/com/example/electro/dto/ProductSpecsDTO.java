package com.example.electro.dto;

import com.example.electro.model.Image;
import com.example.electro.model.ProductSpecs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecsDTO {
    private Integer id;
    private String processor;
    private int memory;
    private String storage;
    private String graphicsCard;
    private String displaySize;
    private int batteryLife;
    private String os;
    private double weight;

}
