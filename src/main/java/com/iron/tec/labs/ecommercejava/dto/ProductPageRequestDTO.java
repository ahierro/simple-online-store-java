package com.iron.tec.labs.ecommercejava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageRequestDTO extends PageRequestDTO {

    private @UUID String categoryId;
    private String queryString;
    private Sort.Direction sortByPrice;

}
