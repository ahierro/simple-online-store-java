package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAO;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.exceptions.NotAuthorized;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
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
    public Mono<ProductDomain> getById(UUID id) {
        return productDAO.findById(id);
    }

    @Override
    public Mono<ProductDomain> createProduct(ProductDomain productDomain) {
        return productDAO.create(productDomain);
    }

    @Override
    public Mono<ProductDomain> updateProduct(ProductDomain productDomain) {
        return productDAO.update(productDomain);
    }

    @Override
    public Mono<PageDomain<ProductDomain>> getProductPage(int page, int size, ProductDomain example, Authentication authentication, Sort.Direction  sortBy) {
        if(BooleanUtils.isTrue(example.getDeleted())
                && (authentication==null ||
                authentication.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("SCOPE_ROLE_ADMIN")))){
            throw new NotAuthorized();
        }
        return productDAO.getProductViewPage(page, size, example, sortBy);
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return productDAO.delete(id);
    }
}
