package com.iron.tec.labs.ecommercejava.db.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AuditableEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @CreatedDate
    @InsertOnlyProperty
    @EqualsAndHashCode.Exclude
    protected LocalDateTime createdAt;

    @EqualsAndHashCode.Exclude
    protected LocalDateTime updatedAt;

    @Override
    public boolean isNew() {
        return updatedAt == null;
    }
}
