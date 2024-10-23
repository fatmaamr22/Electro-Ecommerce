package com.example.electro.dto;

import com.example.electro.enums.OrderState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {
    private Integer id;
    private Date date;
    private CustomerDTO customer;
    private Set<OrderItemDTO> orderItems;
    private Integer totalPrice;
    private OrderState state;
}
