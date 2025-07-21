package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAO;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.exceptions.NotAuthorized;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    @Override
    public ProductDomain getById(UUID id) {
        return productDAO.findById(id);
    }

    @Override
    public ProductDomain createProduct(ProductDomain productDomain) {
        return productDAO.create(productDomain);
    }

    @Override
    public ProductDomain updateProduct(ProductDomain productDomain) {
        return productDAO.update(productDomain);
    }

    @Override
    public PageDomain<ProductDomain> getProductPage(int page, int size, ProductDomain example, Authentication authentication, Sort.Direction  sortBy) {
        return productDAO.getProductViewPage(page, size, example, sortBy);
    }

    @Override
    public void deleteProduct(String id) {
        productDAO.delete(id);
    }
}
