package com.example.electro.mapper;

import com.example.electro.dto.CategoryDTO;
import com.example.electro.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(imports = LocaleContextHolder.class)
public interface CategoryMapper extends GenericMapper<Category, CategoryDTO> {
    CategoryMapper Instance = Mappers.getMapper(CategoryMapper.class);
}
