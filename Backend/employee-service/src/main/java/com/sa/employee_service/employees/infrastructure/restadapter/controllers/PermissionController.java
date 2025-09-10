package com.sa.employee_service.employees.infrastructure.restadapter.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sa.employee_service.employees.application.inputports.FindAllPermissionsInputPort;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.employee_service.employees.infrastructure.restadapter.mappers.PermissionRestMapper;
import com.sa.employee_service.shared.infrastructure.dtos.PermissionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final FindAllPermissionsInputPort findAllPermissionsInputPort;
    private final PermissionRestMapper permissionMapper;

    @Operation(summary = "Obtener todos los permisos", description = "Este endpoint permite la obtención de todos los permisos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permisos obtenidos exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('CREATE_EMPLOYEE_TYPE', 'EDIT_EMPLOYEE_TYPE')")
    public List<PermissionResponse> findPermissions() {
        List<Permission> result = findAllPermissionsInputPort.handle();
        List<PermissionResponse> response = permissionMapper.toResponse(result);
        return response;
    }
}
