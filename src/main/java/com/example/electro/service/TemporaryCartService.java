package com.example.electro.service;

import com.example.electro.dto.CartItemDTO;
import com.example.electro.model.*;
import com.example.electro.repository.CartHasProductRepository;
import com.example.electro.repository.CartRepository;
import com.example.electro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TemporaryCartService {

    private final CartRepository cartDAO;
    private final ProductRepository productDAO;
    private final CartHasProductRepository cartHasProductDAO;

    private static final String SESSION_CART_ATTRIBUTE = "temporaryCart";

    @Autowired
    public TemporaryCartService(ProductRepository productDAO, CartHasProductRepository cartHasProductDAO, CartRepository cartDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.cartHasProductDAO = cartHasProductDAO;
    }

    // Retrieve or create a temporary cart for the session
    private TemporaryCart getOrCreateCart(HttpSession session) {
        TemporaryCart cart = (TemporaryCart) session.getAttribute(SESSION_CART_ATTRIBUTE);
        if (cart == null) {
            cart = new TemporaryCart();
            session.setAttribute(SESSION_CART_ATTRIBUTE, cart);
        }
        return cart;
    }

    // Adds an item to the cart with quantity 1
    public void addCartItem(HttpSession session, int productId) {
        addCartItemWithQuantity(session, productId, 1);
    }

    // Adds an item to the cart with a specific quantity
    public boolean addCartItemWithQuantity(HttpSession session, int productId, int quantity) {
        Optional<Product> productOptional = productDAO.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            TemporaryCart cart = getOrCreateCart(session);

            CartHasProductID cartHasProductID = new CartHasProductID();
            cartHasProductID.setProductId(product.getId());

            CartHasProduct cartHasProduct = new CartHasProduct();
            cartHasProduct.setCartHasProductID(cartHasProductID);
            cartHasProduct.setProduct(product);
            cartHasProduct.setQuantity(quantity);

            cart.addProduct(cartHasProduct);
            return true;
        }
        return false; // Product not found
    }

    // Removes an item from the cart
    public void removeCartItem(HttpSession session, int productId) {
        TemporaryCart cart = getOrCreateCart(session);
        cart.getCartHasProducts().removeIf(item -> item.getCartHasProductID().getProductId().equals(productId));
    }

    // Updates the quantity of an item in the cart
    public boolean setCartItemQuantity(HttpSession session, int productId, int quantity) {
        TemporaryCart cart = getOrCreateCart(session);
        for (CartHasProduct item : cart.getCartHasProducts()) {
            if (item.getCartHasProductID().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                return true;
            }
        }
        return false; // Product not found in the cart
    }

    // Retrieves all items in the temporary cart
    public List<CartItemDTO> getCartItems(HttpSession session) {
        return getOrCreateCart(session).getCartHasProducts().stream().map(CartItemDTO::new).collect(Collectors.toList());
    }

    public void mergeCarts(HttpSession session , int customerId){
        TemporaryCart temporaryCart = getOrCreateCart(session);
        Optional<Cart> cartOptional = cartDAO.findById(customerId);

        if (cartOptional.isPresent() && temporaryCart.getCartHasProducts().size() > 0) {
            // looping over temporary cart items and adding them to them or their quantities to the persisted registered user's cart
            for (CartHasProduct cartHasProduct :  temporaryCart.getCartHasProducts()) {
                mergeCartItemWithQuantity(customerId, cartHasProduct.getCartHasProductID().getProductId(), cartHasProduct.getQuantity());
            }
        }

        emptyCart(session);
    }

    public boolean mergeCartItemWithQuantity(int customerId, int productId, int quantity) {
        Optional<Cart> cartOptional = cartDAO.findById(customerId);
        Optional<Product> productOptional = productDAO.findById(productId);

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

    // Empties the temporary cart
    public void emptyCart(HttpSession session) {
        TemporaryCart cart = getOrCreateCart(session);
        cart.emptyCart();
    }
}
