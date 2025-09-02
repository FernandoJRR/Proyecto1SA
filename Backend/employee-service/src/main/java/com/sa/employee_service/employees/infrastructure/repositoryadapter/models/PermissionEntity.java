package com.sa.employee_service.employees.infrastructure.repositoryadapter.models;

import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PermissionEntity extends AuditorEntity {

    @Column(unique = true, length = 100)
    private String name;
    @Column(unique = true, length = 100)
    private String action;

    public PermissionEntity(String id) {
        super(id);
    }

    public PermissionEntity(String name, String action) {
        this.name = name;
        this.action = action;
    }

}
