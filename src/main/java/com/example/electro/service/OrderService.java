package com.example.electro.service;

import com.example.electro.enums.OrderState;
import com.example.electro.model.*;
import com.example.electro.repository.CouponDAO;
import com.example.electro.repository.CustomerDAO;
import com.example.electro.repository.OrderDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;

public class OrderService {
    private OrderDAO orderDAO;
    private CouponDAO couponDAO;
    private CustomerDAO customerDAO;
    private CouponService couponService;

    public OrderService(OrderDAO orderDAO, CouponDAO couponDAO, CustomerDAO customerDAO) {
        this.orderDAO = orderDAO;
        this.couponDAO = couponDAO;
        this.customerDAO = customerDAO;
    }

    public List<Order> getAllOrders(int pageNo, int size) {
        Page<Order> orders = orderDAO.findAll(PageRequest.of(pageNo, size));
        return orders.getContent();
    }

    public Long countOrders(){
        return orderDAO.count();
    }

    public Order getOrderById(int id) {
        return orderDAO.findById(id).get();
    }

    public List<Order> getOrdersbyCustomerId(int customerId, int pageNo, int size) {
        Customer customer = customerDAO.findById(customerId).get();
        Page<Order> orders = orderDAO.findAllOrdersByCustomerId(customerId, PageRequest.of(pageNo, size));
        return orders.getContent();
    }

    public List<Order> getOrdersbyCouponName(String couponName) {
        return orderDAO.findOrdersByCoupon(couponName);
    }

    public synchronized Order addOrder(Customer customer, String coupon) {
        Set<CartHasProduct> cartHasProducts = customer.getCart().getCartHasProducts();
        Product product = null;
        boolean flag = false;
        for (CartHasProduct cartHasProduct : cartHasProducts) {
            if (cartHasProduct.getProduct().getStock() >= cartHasProduct.getQuantity()) {
                continue;
            } else {
                product = cartHasProduct.getProduct();
                flag = true;
                break;
            }
        }
        if (flag == false) {
            Set<OrderItem> orderItems = new HashSet<>();
            Order order = new Order();
            order.setCustomer(customer);
            for (CartHasProduct cartHasProduct : cartHasProducts) {
                cartHasProduct.getProduct().setStock(cartHasProduct.getProduct().getStock() - cartHasProduct.getQuantity());
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartHasProduct.getProduct());
                orderItem.setQuantity(cartHasProduct.getQuantity());
                orderItem.setCurrentPrice(cartHasProduct.getProduct().getPrice());
                orderItems.add(orderItem);
                order.setTotalPrice((cartHasProduct.getProduct().getPrice()*cartHasProduct.getQuantity()) + order.getTotalPrice());
            }
            if (!coupon.isEmpty()) {
                Coupon c = couponDAO.findCouponByCoupon(coupon);
                if (c != null) {
                    if(c.getEndDate().before(new Date())) {
                        System.out.println("Coupon is Expired");
                        return null;
                    }
                    order.setCoupon(c);
                    int discount = Math.min(order.getTotalPrice()*c.getPercentage()/100, c.getLimitPayment());
                    order.setTotalPrice(order.getTotalPrice() - discount);
                } else {
                    System.out.println("Coupon not found");
                    return null;
                }
            }
            order.setState(OrderState.PENDING);
            CartService cartService = new CartService();
            cartService.emptyCart(customer.getId());
            return orderDAO.saveOrder(order, orderItems);
        } else {
            System.out.println("No Enough Stock for " + product.getName());
            throw new RuntimeException("No Enough Stock for " + product.getName());
        }
    }

}
