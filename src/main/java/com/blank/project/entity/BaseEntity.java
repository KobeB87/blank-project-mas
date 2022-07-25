package com.blank.project.entity;

import lombok.Getter;
import javax.persistence.MappedSuperclass;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("Should be implemented by subclass.");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Should be implemented by subclass.");
    }

    @PrePersist
    private void onPrePersist() {
        this.id = UUID.randomUUID().toString();
        this.created = now();
    }

    @PreUpdate
    private void onPreUpdate() {
        this.updated = now();
    }
}