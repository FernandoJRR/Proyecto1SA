package com.sa.employee_service.employees.infrastructure.repositoryadapter.models;

import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "employeeType")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class EmployeeTypeEntity extends AuditorEntity {

    @Column(unique = true, length = 100)
    private String name;
    /**
     * Un tipo de empleado puede estar asignado a varios empleados
     */
    @OneToMany(mappedBy = "employeeType")
    private List<EmployeeEntity> employees;

    /**
     * Un tipo de empleado tiene múltiples permisos y un permiso puede pertenecer a
     * varios tipos de empleados
     */
    @ManyToMany
    private List<PermissionEntity> permissions;

    public EmployeeTypeEntity(String id, String name) {
        super(id);
        this.name = name;
    }

    public EmployeeTypeEntity(String name, List<PermissionEntity> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public EmployeeTypeEntity(String name) {
        this.name = name;
    }
}
