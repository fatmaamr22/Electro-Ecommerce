package com.example.electro.mapper;

import com.example.electro.dto.ImageDTO;
import com.example.electro.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(imports = LocaleContextHolder.class)
public interface ImageMapper extends GenericMapper<Image, ImageDTO>{
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);
}
