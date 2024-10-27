package com.example.electro.mapper;

import com.example.electro.dto.OrderItemDTO;
import com.example.electro.model.OrderItem;
import com.example.electro.model.OrderItemID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(imports = LocaleContextHolder.class)
public interface OrderItemMapper extends GenericMapper<OrderItem, OrderItemDTO>{
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);
}
