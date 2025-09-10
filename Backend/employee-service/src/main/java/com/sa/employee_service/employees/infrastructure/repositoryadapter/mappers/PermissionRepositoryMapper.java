package com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.domain.Permission;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;

@Mapper(componentModel = "spring")
public interface PermissionRepositoryMapper {
    public PermissionEntity toEntity(Permission permission);
    public Permission toDomain(PermissionEntity permissionEntity);
    public List<Permission> toDomain(List<PermissionEntity> permissionEntity);
    public List<PermissionEntity> toEntityList(List<Permission> permissionEntity);
}
