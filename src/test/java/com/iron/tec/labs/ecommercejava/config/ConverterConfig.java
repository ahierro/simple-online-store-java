package com.iron.tec.labs.ecommercejava.config;

import com.iron.tec.labs.ecommercejava.mappers.category.*;
import com.iron.tec.labs.ecommercejava.mappers.product.*;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.*;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.*;
import com.iron.tec.labs.ecommercejava.mappers.user.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ConverterConfig {

    // Category converters
    @Bean
    public CategoryCreationDTOToDomain categoryCreationDTOToDomain() {
        return new CategoryCreationDTOToDomain();
    }

    @Bean
    public CategoryDomainToDTO categoryDomainToDTO() {
        return new CategoryDomainToDTO();
    }

    @Bean
    public CategoryDomainToEntity categoryDomainToEntity() {
        return new CategoryDomainToEntity();
    }

    @Bean
    public CategoryDTOToDomain categoryDTOToDomain() {
        return new CategoryDTOToDomain();
    }

    @Bean
    public CategoryEntityToDomain categoryEntityToDomain() {
        return new CategoryEntityToDomain();
    }

    @Bean
    public CategoryUpdateDTOToDomain categoryUpdateDTOToDomain() {
        return new CategoryUpdateDTOToDomain();
    }

    @Bean
    public UpdateCategoryMapper updateCategoryMapper() {
        return new UpdateCategoryMapper();
    }

    // Product converters
    @Bean
    public GetProductMapper getProductMapper() {
        return new GetProductMapper();
    }

    @Bean
    public GetProductViewMapper getProductViewMapper() {
        return new GetProductViewMapper();
    }

    @Bean
    public SaveProductMapper saveProductMapper() {
        return new SaveProductMapper();
    }

    @Bean
    public UpdateProductMapper updateProductMapper() {
        return new UpdateProductMapper();
    }

    // User converters
    @Bean
    public GetUserMapper getUserMapper() {
        return new GetUserMapper();
    }

    @Bean
    public RegisterUserMapper registerUserMapper() {
        return new RegisterUserMapper();
    }

    // Purchase order converters
    @Bean
    public GetPurchaseOrderMapper getPurchaseOrderMapper(GetPurchaseOrderLineViewMapper lineViewMapper) {
        return new GetPurchaseOrderMapper(lineViewMapper);
    }

    @Bean
    public GetPurchaseViewOrderMapper getPurchaseViewOrderMapper() {
        return new GetPurchaseViewOrderMapper();
    }

    @Bean
    public SavePurchaseOrderMapper savePurchaseOrderMapper(SavePurchaseOrderLineMapper lineMapper) {
        return new SavePurchaseOrderMapper(lineMapper);
    }

    @Bean
    public UpdatePurchaseOrderMapper updatePurchaseOrderMapper() {
        return new UpdatePurchaseOrderMapper();
    }

    // Purchase order line converters
    @Bean
    public GetPurchaseOrderLineViewMapper getPurchaseOrderLineViewMapper() {
        return new GetPurchaseOrderLineViewMapper();
    }

    @Bean
    public SavePurchaseOrderLineMapper savePurchaseOrderLineMapper() {
        return new SavePurchaseOrderLineMapper();
    }
}
