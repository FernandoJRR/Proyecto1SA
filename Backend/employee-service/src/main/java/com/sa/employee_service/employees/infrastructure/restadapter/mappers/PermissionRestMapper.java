package com.sa.employee_service.employees.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.employee_service.employees.domain.Permission;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.shared.dtos.IdRequestDTO;
import com.sa.employee_service.shared.infrastructure.dtos.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionRestMapper {

    /**
     * Convierte una lista de entidades `Permission` a una lista de
     * PermissionResponseDTO.
     *
     * @param permissions Lista de permisos a convertir.
     * @return Lista de `PermissionResponseDTO` con los datos mapeados.
     */
    public List<PermissionResponse> fromPermissionsToPermissionsReponseDtos(List<PermissionEntity> permissions);

    public List<PermissionResponse> toResponse(List<Permission> permissions);

}
