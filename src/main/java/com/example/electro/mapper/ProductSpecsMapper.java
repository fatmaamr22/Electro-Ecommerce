package com.example.electro.mapper;

import com.example.electro.dto.ProductSpecsDTO;
import com.example.electro.model.ProductSpecs;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;


@Mapper(imports = LocaleContextHolder.class)
public interface ProductSpecsMapper extends GenericMapper<ProductSpecs, ProductSpecsDTO>{
    ProductSpecsMapper INSTANCE = Mappers.getMapper(ProductSpecsMapper.class);
}
