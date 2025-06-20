package com.iron.tec.labs.ecommercejava.services;

import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAO;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.exceptions.NotAuthorized;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

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
