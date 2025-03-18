package com.iron.tec.labs.ecommercejava.config;

import com.iron.tec.labs.ecommercejava.mappers.category.*;
import com.iron.tec.labs.ecommercejava.mappers.product.*;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.*;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.*;
import com.iron.tec.labs.ecommercejava.mappers.user.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
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

    // Purchase order converters
    private final GetPurchaseOrderMapper getPurchaseOrderMapper;
    private final GetPurchaseViewOrderMapper getPurchaseViewOrderMapper;
    private final SavePurchaseOrderMapper savePurchaseOrderMapper;
    private final UpdatePurchaseOrderMapper updatePurchaseOrderMapper;

    // Purchase order line converters
    private final GetPurchaseOrderLineViewMapper getPurchaseOrderLineViewMapper;
    private final SavePurchaseOrderLineMapper savePurchaseOrderLineMapper;

    public ConverterConfig(
            // Category converters
            CategoryCreationDTOToDomain categoryCreationDTOToDomain,
            CategoryDomainToDTO categoryDomainToDTO,
            CategoryDomainToEntity categoryDomainToEntity,
            CategoryDTOToDomain categoryDTOToDomain,
            CategoryEntityToDomain categoryEntityToDomain,
            CategoryUpdateDTOToDomain categoryUpdateDTOToDomain,
            UpdateCategoryMapper updateCategoryMapper,

            // Product converters
            GetProductMapper getProductMapper,
            GetProductViewMapper getProductViewMapper,
            SaveProductMapper saveProductMapper,
            UpdateProductMapper updateProductMapper,

            // User converters
            GetUserMapper getUserMapper,
            RegisterUserMapper registerUserMapper,

            // Purchase order converters
            GetPurchaseOrderMapper getPurchaseOrderMapper,
            GetPurchaseViewOrderMapper getPurchaseViewOrderMapper,
            SavePurchaseOrderMapper savePurchaseOrderMapper,
            UpdatePurchaseOrderMapper updatePurchaseOrderMapper,

            // Purchase order line converters
            GetPurchaseOrderLineViewMapper getPurchaseOrderLineViewMapper,
            SavePurchaseOrderLineMapper savePurchaseOrderLineMapper) {

        // Category converters
        this.categoryCreationDTOToDomain = categoryCreationDTOToDomain;
        this.categoryDomainToDTO = categoryDomainToDTO;
        this.categoryDomainToEntity = categoryDomainToEntity;
        this.categoryDTOToDomain = categoryDTOToDomain;
        this.categoryEntityToDomain = categoryEntityToDomain;
        this.categoryUpdateDTOToDomain = categoryUpdateDTOToDomain;
        this.updateCategoryMapper = updateCategoryMapper;

        // Product converters
        this.getProductMapper = getProductMapper;
        this.getProductViewMapper = getProductViewMapper;
        this.saveProductMapper = saveProductMapper;
        this.updateProductMapper = updateProductMapper;

        // User converters
        this.getUserMapper = getUserMapper;
        this.registerUserMapper = registerUserMapper;

        // Purchase order converters
        this.getPurchaseOrderMapper = getPurchaseOrderMapper;
        this.getPurchaseViewOrderMapper = getPurchaseViewOrderMapper;
        this.savePurchaseOrderMapper = savePurchaseOrderMapper;
        this.updatePurchaseOrderMapper = updatePurchaseOrderMapper;

        // Purchase order line converters
        this.getPurchaseOrderLineViewMapper = getPurchaseOrderLineViewMapper;
        this.savePurchaseOrderLineMapper = savePurchaseOrderLineMapper;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Register all category converters
        registry.addConverter(categoryCreationDTOToDomain);
        registry.addConverter(categoryDomainToDTO);
        registry.addConverter(categoryDomainToEntity);
        registry.addConverter(categoryDTOToDomain);
        registry.addConverter(categoryEntityToDomain);
        registry.addConverter(categoryUpdateDTOToDomain);
        registry.addConverter(updateCategoryMapper);

        // Register all product converters
        registry.addConverter(getProductMapper);
        registry.addConverter(getProductViewMapper);
        registry.addConverter(saveProductMapper);
        registry.addConverter(updateProductMapper);

        // Register all user converters
        registry.addConverter(getUserMapper);
        registry.addConverter(registerUserMapper);

        // Register all purchase order converters
        registry.addConverter(getPurchaseOrderMapper);
        registry.addConverter(getPurchaseViewOrderMapper);
        registry.addConverter(savePurchaseOrderMapper);
        registry.addConverter(updatePurchaseOrderMapper);

        // Register all purchase order line converters
        registry.addConverter(getPurchaseOrderLineViewMapper);
        registry.addConverter(savePurchaseOrderLineMapper);
    }
}
