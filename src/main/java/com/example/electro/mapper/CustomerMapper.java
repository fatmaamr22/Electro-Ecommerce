package com.example.electro.mapper;

import com.example.electro.dto.CustomerDTO;
import com.example.electro.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(imports = LocaleContextHolder.class)
public interface CustomerMapper extends GenericMapper<Customer, CustomerDTO>{
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
}
