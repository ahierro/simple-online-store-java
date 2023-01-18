package com.iron.tec.labs.ecommercejava.mappers;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.ProductUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface UpdateProductMapper extends Converter<ProductUpdateDTO, Product>  {
    @Mapping(target = "description", source = "productDescription")
    @Mapping(target = "name", source = "productName")
    @Mapping(target = "idCategory", source = "categoryId")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Product convert(@NonNull ProductUpdateDTO product);

}