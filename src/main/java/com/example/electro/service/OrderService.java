package com.example.electro.service;

import com.example.electro.dto.OrderDTO;
import com.example.electro.enums.OrderState;
import com.example.electro.mapper.OrderMapper;
import com.example.electro.model.*;
import com.example.electro.repository.CouponRepository;
import com.example.electro.repository.CustomerRepository;
import com.example.electro.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private OrderRepository orderRepo;
    private CouponRepository couponRepo;
    private CustomerRepository customerRepo;
    private CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepo, CouponRepository couponRepo, CustomerRepository customerRepo, CartService cartService) {
        this.orderRepo = orderRepo;
        this.couponRepo = couponRepo;
        this.customerRepo = customerRepo;
        this.cartService = cartService;
    }

    public List<OrderDTO> getAllOrders(int pageNo, int size) {
        Page<Order> orders = orderRepo.findAll(PageRequest.of(pageNo, size));
        return OrderMapper.INSTANCE.toDTOs(orders.getContent());
    }

    public Long countOrders(){
        return orderRepo.count();
    }

    public OrderDTO getOrderById(int id) {
        return OrderMapper.INSTANCE.toDTO(orderRepo.findById(id).get());
    }

    public List<OrderDTO> getOrdersbyCustomerId(int customerId, int pageNo, int size) {
        Customer customer = customerRepo.findById(customerId).get();
        Page<Order> orders = orderRepo.findAllOrdersByCustomerId(customerId, PageRequest.of(pageNo, size));
        return OrderMapper.INSTANCE.toDTOs(orders.getContent());
    }

    public List<OrderDTO> getOrdersbyCouponName(String couponName) {
        return OrderMapper.INSTANCE.toDTOs(orderRepo.findOrdersByCoupon(couponName));
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
                Coupon c = couponRepo.findCouponByCoupon(coupon);
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
            cartService.emptyCart(customer.getId());
            return orderRepo.saveOrder(order, orderItems);
        } else {
            System.out.println("No Enough Stock for " + product.getName());
            throw new RuntimeException("No Enough Stock for " + product.getName());
        }
    }

}
