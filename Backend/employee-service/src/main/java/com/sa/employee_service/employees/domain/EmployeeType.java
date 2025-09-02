package com.sa.employee_service.employees.domain;

import java.util.List;
import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeType extends Auditor {

    private String name;
    private List<Permission> permissions;

    public EmployeeType(UUID id, String name) {
        super(id);
        this.name = name;
    }
}
