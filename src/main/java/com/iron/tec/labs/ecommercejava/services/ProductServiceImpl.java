package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAO;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
    public Mono<PageResponseDTO<ProductDTO>> getProductPage(PageRequestDTO pageRequest) {
        return productDAO.getProductViewPage(pageRequest.getPage(), pageRequest.getSize())
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
