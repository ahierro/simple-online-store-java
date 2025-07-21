package com.iron.tec.labs.ecommercejava.db.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity implements Persistable<UUID>{

    @Id
    private UUID id;

    @CreatedDate
    @Column(updatable = false)
    @EqualsAndHashCode.Exclude
    protected LocalDateTime createdAt;

    @EqualsAndHashCode.Exclude
    protected LocalDateTime updatedAt;

    @Override
    public boolean isNew() {
        return updatedAt == null;
    }

}
