package com.sa.employee_service.employees.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.inputports.FindAllPermissionsInputPort;
import com.sa.employee_service.employees.application.outputports.FindAllPermissionsOutputPort;
import com.sa.employee_service.employees.domain.Permission;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllPermissionsUseCase implements FindAllPermissionsInputPort {

    private final FindAllPermissionsOutputPort findAllPermissionsOutputPort;

    @Override
    public List<Permission> handle() {
        return findAllPermissionsOutputPort.findAll();
    }

}
