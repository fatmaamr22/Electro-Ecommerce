package com.example.electro.mapper;

import com.example.electro.dto.ProductWithSpecsDTO;
import com.example.electro.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(uses = {ProductSpecsMapper.class, CategoryMapper.class}, imports = LocaleContextHolder.class)
public interface ProductWithSpecsMapper extends GenericMapper<Product, ProductWithSpecsDTO>{
    ProductWithSpecsMapper INSTANCE = Mappers.getMapper(ProductWithSpecsMapper.class);
}
