package com.sa.employee_service.employees.application.inputports;

import org.springframework.validation.annotation.Validated;

import com.sa.employee_service.employees.application.dtos.CreatePermissionDTO;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.shared.exceptions.DuplicatedEntryException;

import jakarta.validation.Valid;

@Validated
public interface CreatePermissionInputPort {
    public Permission handle(@Valid CreatePermissionDTO createPermissionDTO) throws DuplicatedEntryException;
}
