package com.iron.tec.labs.ecommercejava.config;

import com.iron.tec.labs.ecommercejava.mappers.category.*;
import com.iron.tec.labs.ecommercejava.mappers.product.*;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.*;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.*;
import com.iron.tec.labs.ecommercejava.mappers.user.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;

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

    @Bean
    public ProductUpdateDTOToDomain productUpdateDTOToDomain() {
        return new ProductUpdateDTOToDomain();
    }

    @Bean
    public ProductEntityToDomain productEntityToDomain() {
        return new ProductEntityToDomain();
    }

    @Bean
    public ProductDTOToDomain productDTOToDomain() {
        return new ProductDTOToDomain();
    }

    @Bean
    public ProductDomainToEntity productDomainToEntity() {
        return new ProductDomainToEntity();
    }

    @Bean
    public ProductCreationDTOToDomain productCreationDTOToDomain() {
        return new ProductCreationDTOToDomain();
    }

    @Bean
    public ProductDomainToDTO productDomainToDTO() {
        return new ProductDomainToDTO();
    }

    @Bean
    public ProductDomainToProductView productDomainToProductView() {
        return new ProductDomainToProductView();
    }

    @Bean
    public ProductViewToProductDomain productViewToProductDomain() {
        return new ProductViewToProductDomain();
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

    @Bean
    public PurchaseOrderDomainToEntity purchaseOrderDomainToEntity(PurchaseOrderLineDomainToEntity conversionService) {
        return new PurchaseOrderDomainToEntity(conversionService);
    }

    @Bean
    public PurchaseOrderEntityToDomain purchaseOrderEntityToDomain(PurchaseOrderLineEntityToDomain purchaseOrderLineEntityToDomain,
                                                                   PurchaseOrderLineViewToDomain purchaseOrderLineViewToDomain,
                                                                   AppUserEntityToDomain appUserEntityToDomain) {
        return new PurchaseOrderEntityToDomain(purchaseOrderLineEntityToDomain, purchaseOrderLineViewToDomain, appUserEntityToDomain);
    }

    @Bean
    public PurchaseOrderLineEntityToDomain purchaseOrderLineEntityToDomain() {
        return new PurchaseOrderLineEntityToDomain();
    }

    @Bean
    public PurchaseOrderLineViewToDomain purchaseOrderLineViewToDomain() {
        return new PurchaseOrderLineViewToDomain();
    }

    @Bean
    public AppUserEntityToDomain appUserEntityToDomain() {
        return new AppUserEntityToDomain();
    }

    @Bean
    public GetPurchaseOrderLineViewMapper getPurchaseOrderLineViewMapper() {
        return new GetPurchaseOrderLineViewMapper();
    }

    @Bean
    public PurchaseOrderLineCreationDTOToDomain purchaseOrderLineCreationDTOToDomain() {
        return new PurchaseOrderLineCreationDTOToDomain();
    }

    @Bean
    public SavePurchaseOrderLineMapper savePurchaseOrderLineMapper() {
        return new SavePurchaseOrderLineMapper();
    }

    @Bean
    public PurchaseOrderLineDomainToEntity purchaseOrderLineDomainToEntity() {
        return new PurchaseOrderLineDomainToEntity();
    }

    @Bean
    public PurchaseOrderLineDomainToDTO purchaseOrderLineDomainToDTO() {
        return new PurchaseOrderLineDomainToDTO();
    }

    @Bean
    public PurchaseOrderDomainToDTO purchaseOrderDomainToDTO(PurchaseOrderLineDomainToDTO purchaseOrderLineDomainToDTO,
                                                             AppUserEntityToDTO appUserEntityToDTO) {
        return new PurchaseOrderDomainToDTO(purchaseOrderLineDomainToDTO, appUserEntityToDTO);
    }

    @Bean
    public PurchaseOrderCreationDTOToDomain purchaseOrderCreationDTOToDomain(PurchaseOrderLineCreationDTOToDomain conversionService) {
        return new PurchaseOrderCreationDTOToDomain(conversionService);
    }

    @Bean
    public PurchaseOrderPatchDTOToDomain purchaseOrderPatchDTOToDomain() {
        return new PurchaseOrderPatchDTOToDomain();
    }

    @Bean
    public PurchaseOrderDomainToViewDTO purchaseOrderDomainToViewDTO() {
        return new PurchaseOrderDomainToViewDTO();
    }

    @Bean
    public AppUserEntityToDTO appUserEntityToDTO() {
        return new AppUserEntityToDTO();
    }
}
