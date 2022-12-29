package com.iron.tec.labs.ecommercejava.mappers;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface GetProductMapper extends Converter<Product, ProductDTO> {

    @Mapping(target = "productDescription", source = "description")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "productId", source = "id")
    ProductDTO convert(@NonNull Product product);
}