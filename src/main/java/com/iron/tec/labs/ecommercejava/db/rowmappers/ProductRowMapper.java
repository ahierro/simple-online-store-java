package com.iron.tec.labs.ecommercejava.db.rowmappers;

import com.iron.tec.labs.ecommercejava.dto.CategoryDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.BiFunction;

@Service
@AllArgsConstructor
public class ProductRowMapper implements BiFunction<Row, RowMetadata, ProductDTO> {

    private final ColumnConverter converter;

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link User} stored in the database.
     */
    @Override
    public ProductDTO apply(Row row, RowMetadata prefix) {
        return ProductDTO.builder()
                .productId(converter.fromRow(row, "id_product", String.class))
                .productName(converter.fromRow(row, "product_name", String.class))
                .productDescription(converter.fromRow(row, "product_description", String.class))
                .stock(converter.fromRow(row, "stock", Integer.class))
                .price(converter.fromRow(row, "price", BigDecimal.class))
                .smallImageUrl(converter.fromRow(row, "small_image_url", String.class))
                .bigImageUrl(converter.fromRow(row, "small_image_url", String.class))
                .deleted(converter.fromRow(row, "deleted", Boolean.class))
                .category(CategoryDTO.builder()
                        .id(converter.fromRow(row, "id_category", String.class))
                        .name(converter.fromRow(row, "category_name", String.class))
                        .description(converter.fromRow(row, "category_description", String.class)).build())
                .build();
    }
}