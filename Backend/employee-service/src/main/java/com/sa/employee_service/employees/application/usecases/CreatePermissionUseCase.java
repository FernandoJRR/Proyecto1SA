package com.sa.employee_service.employees.application.usecases;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.dtos.CreatePermissionDTO;
import com.sa.employee_service.employees.application.inputports.CreatePermissionInputPort;
import com.sa.employee_service.employees.application.outputports.CreatePermissionOutputPort;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.shared.exceptions.DuplicatedEntryException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@Validated
@RequiredArgsConstructor
public class CreatePermissionUseCase implements CreatePermissionInputPort {

    private final CreatePermissionOutputPort createPermissionOutputPort;

    public Permission handle(CreatePermissionDTO createPermissionDTO) throws DuplicatedEntryException {
        Permission genPermission = Permission.create(createPermissionDTO.getName(), createPermissionDTO.getAction());

        return createPermissionOutputPort.createPermission(genPermission);
    }

}
