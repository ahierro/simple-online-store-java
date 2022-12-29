package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAO;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.ProductCreationDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductUpdateDTO;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    private final ConversionService conversionService;

    public ProductServiceImpl(ProductDAO productDAO, ConversionService conversionService) {
        this.productDAO = productDAO;
        this.conversionService = conversionService;
    }

    public Mono<ProductDTO> createProduct(ProductCreationDTO productCreationDTO) {
        return productDAO.create(conversionService.convert(productCreationDTO, Product.class))
                .mapNotNull(product -> conversionService.convert(product, ProductDTO.class));
    }
    @Override
    public Mono<ProductDTO> updateProduct(String id, ProductUpdateDTO productCreationDTO) {
        Product product = conversionService.convert(productCreationDTO, Product.class);
        if(product!=null){
            product.setId(UUID.fromString(id));
        }
        return productDAO.update(product)
                .mapNotNull(productUpdated -> conversionService.convert(productUpdated, ProductDTO.class));
    }

    @Override
    public Flux<ProductDTO> getAll() {
        return productDAO.getAll().mapNotNull(product -> conversionService.convert(product, ProductDTO.class));
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return productDAO.delete(id);
    }
}
