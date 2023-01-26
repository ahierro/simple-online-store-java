package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAO;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.exceptions.NotAuthorized;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    private final ConversionService conversionService;

    @Override
    public Mono<ProductDTO> getById(UUID id) {
        return productDAO.findById(id);
    }

    public Mono<ProductDTO> createProduct(ProductCreationDTO productCreationDTO) {
        return productDAO.create(conversionService.convert(productCreationDTO, Product.class))
                .mapNotNull(product -> conversionService.convert(product, ProductDTO.class));
    }

    @Override
    public Mono<ProductDTO> updateProduct(String id, ProductUpdateDTO productCreationDTO) {
        Product product = conversionService.convert(productCreationDTO, Product.class);
        if (product != null) {
            product.setId(UUID.fromString(id));
        }
        return productDAO.update(product)
                .mapNotNull(productUpdated -> conversionService.convert(productUpdated, ProductDTO.class));
    }

    @Override
    public Mono<PageResponseDTO<ProductDTO>> getProductPage(ProductPageRequestDTO pageRequest, Authentication authentication) {
        if(BooleanUtils.isTrue(pageRequest.getDeleted())
                && (authentication==null ||
                authentication.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("SCOPE_ROLE_ADMIN")))){
            throw new NotAuthorized();
        }
        UUID categoryId = (pageRequest.getCategoryId() == null) ? null : UUID.fromString(pageRequest.getCategoryId());
        return productDAO.getProductViewPage(pageRequest.getPage(), pageRequest.getSize(),
                        ProductView.builder()
                                .idCategory(categoryId)
                                .deleted(pageRequest.getDeleted())
                                .productDescription(pageRequest.getQueryString()).build(),
                        pageRequest.getSortByPrice())
                .mapNotNull(page ->
                        new PageResponseDTO<>(
                                page.getContent().stream()
                                        .map(x -> conversionService.convert(x, ProductDTO.class)).toList()
                                , page.getPageable()
                                , page.getTotalPages()));
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return productDAO.delete(id);
    }
}
