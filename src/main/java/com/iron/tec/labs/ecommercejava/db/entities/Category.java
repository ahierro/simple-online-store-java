package com.iron.tec.labs.ecommercejava.db.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Table("CATEGORY")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends AuditableEntity {

    private String name;
    private String description;
    private Boolean deleted;

}
