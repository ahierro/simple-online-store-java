package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.dto.CategoryDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomProductRepositoryImpl implements CustomProductRepository{
    private final EntityManager entityManager;

    @Override
    public Optional<ProductDTO> findById(UUID id) {
        try {
            Query query = entityManager.createNativeQuery("""
                        SELECT p.id as id_product,
                               p.name as product_name,
                               p.price as price,
                               p.stock as stock,
                               p.description as product_description,
                               p.big_image_url as big_image_url,
                               p.small_image_url as small_image_url,
                               c.name as category_name,
                               c.description as category_description,
                               c.created_at as category_created_at,
                               p.id_category as id_category,
                               p.created_at as product_created_at,
                               p.updated_at as product_updated_at,
                               c.updated_at as category_updated_at
                        FROM product p INNER JOIN category c on c.id = p.id_category
                       where p.id = :id
                    """);
            query.setParameter("id", id);
            Object[] result = (Object[]) query.getSingleResult();
            
            return Optional.of(ProductDTO.builder()
                    .productId((result[0]).toString())
                    .productName((String) result[1])
                    .price((BigDecimal) result[2])
                    .stock((Integer) result[3])
                    .productDescription((String) result[4])
                    .bigImageUrl((String) result[5])
                    .smallImageUrl((String) result[6])
                    .updatedAt(result[12] != null ? ((Timestamp) result[12]).toLocalDateTime() : null)
                    .createdAt(result[11] != null ? ((Timestamp) result[11]).toLocalDateTime() : null)
                    .category(CategoryDTO.builder()
                            .name((String) result[7])
                            .description((String) result[8])
                            .createdAt(result[9] != null ? ((Timestamp) result[9]).toLocalDateTime() : null)
                            .updatedAt(result[13] != null ? ((Timestamp) result[13]).toLocalDateTime() : null)
                            .id((result[10]).toString())
                            .build())
                    .build());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}