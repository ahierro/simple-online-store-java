package com.iron.tec.labs.ecommercejava.config;

import com.iron.tec.labs.ecommercejava.mappers.category.*;
import com.iron.tec.labs.ecommercejava.mappers.product.GetProductMapper;
import com.iron.tec.labs.ecommercejava.mappers.product.GetProductViewMapper;
import com.iron.tec.labs.ecommercejava.mappers.product.SaveProductMapper;
import com.iron.tec.labs.ecommercejava.mappers.product.UpdateProductMapper;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.*;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.*;
import com.iron.tec.labs.ecommercejava.mappers.user.AppUserEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.user.GetUserMapper;
import com.iron.tec.labs.ecommercejava.mappers.user.RegisterUserMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@AllArgsConstructor
public class ConverterConfig implements WebFluxConfigurer {

    // Category converters
    private final CategoryCreationDTOToDomain categoryCreationDTOToDomain;
    private final CategoryDomainToDTO categoryDomainToDTO;
    private final CategoryDomainToEntity categoryDomainToEntity;
    private final CategoryDTOToDomain categoryDTOToDomain;
    private final CategoryEntityToDomain categoryEntityToDomain;
    private final CategoryUpdateDTOToDomain categoryUpdateDTOToDomain;
    private final UpdateCategoryMapper updateCategoryMapper;

    // Product converters
    private final GetProductMapper getProductMapper;
    private final GetProductViewMapper getProductViewMapper;
    private final SaveProductMapper saveProductMapper;
    private final UpdateProductMapper updateProductMapper;

    // User converters
    private final GetUserMapper getUserMapper;
    private final RegisterUserMapper registerUserMapper;
    private final AppUserEntityToDomain appUserEntityToDomain;

    // Purchase order converters
    private final PurchaseOrderDomainToDTO purchaseOrderDomainToDTO;
    private final PurchaseOrderCreationDTOToDomain purchaseOrderCreationDTOToDomain;
    private final PurchaseOrderPatchDTOToDomain purchaseOrderPatchDTOToDomain;
    private final PurchaseOrderDomainToEntity purchaseOrderDomainToEntity;
    private final PurchaseOrderEntityToDomain purchaseOrderEntityToDomain;
    private final PurchaseOrderViewToDomain purchaseOrderViewToDomain;
    private final PurchaseOrderDomainToView purchaseOrderDomainToView;
    private final PurchaseOrderDomainToViewDTO purchaseOrderDomainToViewDTO;

    // Purchase order line converters
    private final PurchaseOrderLineDomainToDTO purchaseOrderLineDomainToDTO;
    private final PurchaseOrderLineCreationDTOToDomain purchaseOrderLineCreationDTOToDomain;
    private final PurchaseOrderLineDomainToEntity purchaseOrderLineDomainToEntity;
    private final PurchaseOrderLineEntityToDomain purchaseOrderLineEntityToDomain;
    private final PurchaseOrderLineViewToDomain purchaseOrderLineViewToDomain;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Category
        registry.addConverter(categoryCreationDTOToDomain);
        registry.addConverter(categoryDomainToDTO);
        registry.addConverter(categoryDomainToEntity);
        registry.addConverter(categoryDTOToDomain);
        registry.addConverter(categoryEntityToDomain);
        registry.addConverter(categoryUpdateDTOToDomain);
        registry.addConverter(updateCategoryMapper);

        // Product
        registry.addConverter(getProductMapper);
        registry.addConverter(getProductViewMapper);
        registry.addConverter(saveProductMapper);
        registry.addConverter(updateProductMapper);

        // User
        registry.addConverter(getUserMapper);
        registry.addConverter(registerUserMapper);
        registry.addConverter(appUserEntityToDomain);

        // Purchase orders
        registry.addConverter(purchaseOrderDomainToDTO);
        registry.addConverter(purchaseOrderCreationDTOToDomain);
        registry.addConverter(purchaseOrderPatchDTOToDomain);
        registry.addConverter(purchaseOrderDomainToEntity);
        registry.addConverter(purchaseOrderEntityToDomain);
        registry.addConverter(purchaseOrderViewToDomain);
        registry.addConverter(purchaseOrderDomainToView);
        registry.addConverter(purchaseOrderDomainToViewDTO);

        // Purchase order lines
        registry.addConverter(purchaseOrderLineDomainToDTO);
        registry.addConverter(purchaseOrderLineCreationDTOToDomain);
        registry.addConverter(purchaseOrderLineDomainToEntity);
        registry.addConverter(purchaseOrderLineEntityToDomain);
        registry.addConverter(purchaseOrderLineViewToDomain);
    }

}
