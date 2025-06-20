package com.iron.tec.labs.ecommercejava.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.iron.tec.labs.ecommercejava.mappers.category.CategoryCreationDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.category.CategoryDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.category.CategoryDomainToDTO;
import com.iron.tec.labs.ecommercejava.mappers.category.CategoryDomainToEntity;
import com.iron.tec.labs.ecommercejava.mappers.category.CategoryEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.category.CategoryUpdateDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.category.UpdateCategoryMapper;
import com.iron.tec.labs.ecommercejava.mappers.product.GetProductMapper;
import com.iron.tec.labs.ecommercejava.mappers.product.GetProductViewMapper;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductCreationDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductDomainToDTO;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductDomainToEntity;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductDomainToProductView;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductUpdateDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductViewToProductDomain;
import com.iron.tec.labs.ecommercejava.mappers.product.SaveProductMapper;
import com.iron.tec.labs.ecommercejava.mappers.product.UpdateProductMapper;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.GetPurchaseOrderMapper;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.GetPurchaseViewOrderMapper;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.PurchaseOrderCreationDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.PurchaseOrderDomainToDTO;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.PurchaseOrderDomainToEntity;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.PurchaseOrderDomainToViewDTO;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.PurchaseOrderEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.PurchaseOrderPatchDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.SavePurchaseOrderMapper;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.UpdatePurchaseOrderMapper;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.GetPurchaseOrderLineViewMapper;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineCreationDTOToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineDomainToDTO;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineDomainToEntity;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineViewToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.SavePurchaseOrderLineMapper;
import com.iron.tec.labs.ecommercejava.mappers.user.AppUserEntityToDTO;
import com.iron.tec.labs.ecommercejava.mappers.user.AppUserEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.user.GetUserMapper;
import com.iron.tec.labs.ecommercejava.mappers.user.RegisterUserMapper;

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
