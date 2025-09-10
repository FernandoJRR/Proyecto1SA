package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.employees.application.outputports.FindAllPermissionsOutputPort;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.PermissionRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.PermissionRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindAllPermissionsAdapter implements FindAllPermissionsOutputPort {

    private final PermissionRepository permissionRepository;
    private final PermissionRepositoryMapper permissionRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        List<PermissionEntity>entities = permissionRepository.findAll();
        return permissionRepositoryMapper.toDomain(entities);
    }

}
