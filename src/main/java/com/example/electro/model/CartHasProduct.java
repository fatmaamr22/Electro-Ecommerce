package com.example.electro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "cart_has_product")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CartHasProduct {

    @EmbeddedId
    private CartHasProductID cartHasProductID;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    private Cart cart;

    @Column(nullable = false)
    @Min(1)
    private int quantity;

    @EqualsAndHashCode.Include
    public Integer getProductId() {
        return this.cartHasProductID.getProductId();
    }
}
