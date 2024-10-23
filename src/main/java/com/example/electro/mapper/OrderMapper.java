package com.example.electro.mapper;

import com.example.electro.dto.OrderDTO;
import com.example.electro.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

@Mapper(imports = LocaleContextHolder.class)
public interface OrderMapper extends GenericMapper<Order, OrderDTO> {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
}
