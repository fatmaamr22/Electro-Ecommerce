package com.example.electro.model;

import java.util.HashSet;
import java.util.Set;

public class TemporaryCart {
    private Set<CartHasProduct> cartHasProducts = new HashSet<>();

    public Set<CartHasProduct> getCartHasProducts() {
        return cartHasProducts;
    }

    public void addProduct(CartHasProduct cartHasProduct) {
        cartHasProducts.add(cartHasProduct);
    }

    public void removeProduct(CartHasProduct cartHasProduct) {
        cartHasProducts.remove(cartHasProduct);
    }

    public void emptyCart() {
        cartHasProducts.clear();
    }
}
