package com.example.electro.mapper;


import com.example.electro.dto.ProductDTO;
import com.example.electro.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(imports = LocaleContextHolder.class)
public interface ProductMapper extends GenericMapper<Product, ProductDTO> {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
}
