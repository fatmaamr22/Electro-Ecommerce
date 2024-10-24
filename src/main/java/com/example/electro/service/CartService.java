package com.example.electro.service;

import com.example.electro.dto.CartItemDTO;
import com.example.electro.model.Cart;
import com.example.electro.model.CartHasProduct;
import com.example.electro.model.CartHasProductID;
import com.example.electro.model.Product;
import com.example.electro.repository.CartHasProductRepository;
import com.example.electro.repository.CartRepository;
import com.example.electro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    private final CartRepository cartDAO;
    private final CartHasProductRepository cartHasProductDAO;
    private final ProductRepository productDAO;

    @Autowired
    public CartService(CartRepository cartDAO, CartHasProductRepository cartHasProductDAO, ProductRepository productDAO) {
        this.cartDAO = cartDAO;
        this.cartHasProductDAO = cartHasProductDAO;
        this.productDAO = productDAO;
    }



    // Adds an item to the cart with quantity 1
    public void addCartItem(int customerId, int productId) {
        addCartItemWithQuantity(customerId, productId, 1);
    }

    /*
    // Adds an item to the cart with a specific quantity
    public boolean addCartItemWithQuantity(int customerId, int productId, int quantity) {
        Optional<Cart> cartOptional = cartDAO.findById(customerId);
        Optional<Product> productOptional = productDAO.findById(productId);

        if (cartOptional.isPresent() && productOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Product product = productOptional.get();

            CartHasProductID cartHasProductID = new CartHasProductID();
            cartHasProductID.setCartId(cart.getId());
            cartHasProductID.setProductId(product.getId());

            CartHasProduct cartHasProduct = new CartHasProduct();
            cartHasProduct.setCartHasProductID(cartHasProductID);
            cartHasProduct.setCart(cart);
            cartHasProduct.setProduct(product);
            cartHasProduct.setQuantity(quantity);

            cartHasProductDAO.save(cartHasProduct);
            return true;
        }
        return false; // Cart or Product not found
    }
*/
    /*
    * a new function if needed to add the quantities to existing product not setting it
    */
    public boolean addCartItemWithQuantity(int customerId, int productId, int quantity) {
        Optional<Cart> cartOptional = cartDAO.findById(customerId);
        Optional<Product> productOptional = productDAO.findById( productId);

        if (cartOptional.isPresent() && productOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Product product = productOptional.get();

            // Find if the product is already in the cart
            CartHasProductID cartHasProductID = new CartHasProductID();
            cartHasProductID.setCartId(cart.getId());
            cartHasProductID.setProductId(product.getId());

            Optional<CartHasProduct> existingCartProductOptional = cartHasProductDAO.findById(cartHasProductID);

            CartHasProduct cartHasProduct;
            if (existingCartProductOptional.isPresent()) {
                // If the product is already in the cart, add to the existing quantity
                cartHasProduct = existingCartProductOptional.get();
                cartHasProduct.setQuantity(cartHasProduct.getQuantity() + quantity);
            } else {
                // If the product is not in the cart, create a new entry
                cartHasProduct = new CartHasProduct();
                cartHasProduct.setCartHasProductID(cartHasProductID);
                cartHasProduct.setCart(cart);
                cartHasProduct.setProduct(product);
                cartHasProduct.setQuantity(quantity);
            }

            cartHasProductDAO.save(cartHasProduct);
            return true;
        }

        return false; // Cart or Product not found
    }


    // Removes an item from the cart
    public void removeCartItem(int customerId, int productId) {
        Optional<Cart> cartOptional = cartDAO.findById(customerId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            CartHasProductID cartHasProductID = new CartHasProductID();
            cartHasProductID.setCartId(cart.getId());
            cartHasProductID.setProductId(productId);

            cartHasProductDAO.deleteById(cartHasProductID);
        }
    }

    // Updates the quantity of an item in the cart
    public boolean setCartItemQuantity(int customerId, int productId, int quantity) {
        Optional<Cart> cartOptional = cartDAO.findById(customerId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            CartHasProductID cartHasProductID = new CartHasProductID();
            cartHasProductID.setCartId(cart.getId());
            cartHasProductID.setProductId(productId);

            Optional<CartHasProduct> cartHasProductOptional = cartHasProductDAO.findById(cartHasProductID);
            if (cartHasProductOptional.isPresent()) {
                CartHasProduct cartHasProduct = cartHasProductOptional.get();
                cartHasProduct.setQuantity(quantity);
                cartHasProductDAO.save(cartHasProduct);
                return true;
            }
        }
        return false; // Cart or Product not found
    }

    // Retrieves all items in a customer's cart
    public List<CartItemDTO> getCartItems(int customerId) {
        Optional<Cart> cartOptional = cartDAO.findById(customerId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            List<CartHasProduct> cartHasProducts = cartHasProductDAO.findByCartId(cart.getId());

            return cartHasProducts.stream()
                    .map(CartItemDTO::new)
                    .collect(Collectors.toList());
        }
        return null; // Cart not found
    }

    // Empties the cart
    public void emptyCart(int customerId) {
        Optional<Cart> cartOptional = cartDAO.findById(customerId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            List<CartHasProduct> cartItems = cartHasProductDAO.findByCartId(cart.getId());
            cartHasProductDAO.deleteAll(cartItems);
        }
    }

    // Retrieves a specific item in the cart
    public CartItemDTO getItem(int customerId, int itemId) {
        Optional<Cart> cartOptional = cartDAO.findById(customerId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            CartHasProductID cartHasProductID = new CartHasProductID();
            cartHasProductID.setCartId(cart.getId());
            cartHasProductID.setProductId(itemId);

            Optional<CartHasProduct> cartHasProductOptional = cartHasProductDAO.findById(cartHasProductID);
            if (cartHasProductOptional.isPresent()) {
                CartHasProduct cartHasProduct = cartHasProductOptional.get();
                return new CartItemDTO(cartHasProduct);
            }
        }
        return null; // Cart or Product not found
    }
}
