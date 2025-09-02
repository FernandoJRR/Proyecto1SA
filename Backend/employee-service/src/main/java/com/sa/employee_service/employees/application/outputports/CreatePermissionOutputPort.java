package com.sa.employee_service.employees.application.outputports;

import com.sa.employee_service.employees.domain.Permission;

public interface CreatePermissionOutputPort {
    public Permission createPermission(Permission permission);
}
