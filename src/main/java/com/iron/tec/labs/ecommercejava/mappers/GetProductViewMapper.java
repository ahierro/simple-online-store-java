package com.iron.tec.labs.ecommercejava.mappers;

import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface GetProductViewMapper extends Converter<ProductView, ProductDTO> {

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "createdAt", source = "productCreatedAt")
    @Mapping(target = "category", expression = "java( com.iron.tec.labs.ecommercejava.dto.CategoryDTO.builder().id(product.getIdCategory().toString()).name(product.getCategoryName()).description(product.getCategoryDescription()).createdAt(product.getCategoryCreatedAt()).build() )")
    ProductDTO convert(@NonNull ProductView product);
}