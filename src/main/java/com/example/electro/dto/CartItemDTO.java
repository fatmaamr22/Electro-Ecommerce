package com.example.electro.dto;


import com.example.electro.model.CartHasProduct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CartItemDTO {
    private Integer id;
    private String name;
    private int price;
    private String image;
    private int quantity;

    public CartItemDTO(CartHasProduct item){
        this.quantity = item.getQuantity();
        this.id = item.getProduct().getId();
        this.name = item.getProduct().getName();
        this.price = item.getProduct().getPrice();
        this.image = item.getProduct().getImage();
    }

}
