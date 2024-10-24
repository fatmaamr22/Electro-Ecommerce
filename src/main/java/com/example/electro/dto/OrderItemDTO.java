package com.example.electro.dto;

import com.example.electro.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDTO {
    private Integer id;
    private Integer quantity;
    private Integer price;
    private ProductDTO product;
    private int currentPrice;
}
