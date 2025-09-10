package com.sa.employee_service.employees.application.inputports;

import java.util.List;

import com.sa.employee_service.employees.domain.Permission;

public interface FindAllPermissionsInputPort {
    public List<Permission> handle();
}
