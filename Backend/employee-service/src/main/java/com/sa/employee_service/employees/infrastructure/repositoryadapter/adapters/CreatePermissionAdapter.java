package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import com.sa.employee_service.employees.application.outputports.CreatePermissionOutputPort;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.PermissionRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.PermissionRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreatePermissionAdapter implements CreatePermissionOutputPort {

    private final PermissionRepository permissionRepository;
    private final PermissionRepositoryMapper permissionRepositoryMapper;

    @Override
    public Permission createPermission(Permission permission) {
        PermissionEntity createdPermission = permissionRepository.save(permissionRepositoryMapper.toEntity(permission));
        return permissionRepositoryMapper.toDomain(createdPermission);
    }

}
