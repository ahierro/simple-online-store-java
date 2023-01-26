package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.ProductCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface SaveProductMapper extends Converter<ProductCreationDTO, Product>  {
    @Mapping(target = "description", source = "productDescription")
    @Mapping(target = "name", source = "productName")
    @Mapping(target = "id", source = "productId")
    @Mapping(target = "idCategory", source = "categoryId")
    Product convert(@NonNull ProductCreationDTO product);

}