package com.example.electro.model;

import java.util.HashSet;
import java.util.Set;

public class TemporaryCart {
    private Set<CartHasProduct> cartHasProducts = new HashSet<>();

    public Set<CartHasProduct> getCartHasProducts() {
        return cartHasProducts;
    }

    public void addProduct(CartHasProduct cartHasProduct) {
        // If product already exists in the cart, increment its quantity
        if (cartHasProducts.contains(cartHasProduct)) {
            // Find the existing product and update its quantity
            for (CartHasProduct existingProduct : cartHasProducts) {
                if (existingProduct.equals(cartHasProduct)) {
                    existingProduct.setQuantity(existingProduct.getQuantity() + cartHasProduct.getQuantity());
                    break;
                }
            }
        } else {
            // If product is not already in the cart, add it
            cartHasProducts.add(cartHasProduct);
        }
    }

    public void removeProduct(CartHasProduct cartHasProduct) {
        cartHasProducts.remove(cartHasProduct);
    }

    public void emptyCart() {
        cartHasProducts.clear();
    }
}
