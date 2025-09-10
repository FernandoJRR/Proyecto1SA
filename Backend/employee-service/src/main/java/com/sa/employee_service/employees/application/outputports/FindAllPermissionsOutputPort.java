package com.sa.employee_service.employees.application.outputports;

import java.util.List;

import com.sa.employee_service.employees.domain.Permission;

public interface FindAllPermissionsOutputPort {
    public List<Permission> findAll();
}
