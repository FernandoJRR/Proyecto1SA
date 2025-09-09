package com.sa.employee_service.employees.application.usecases;

import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.dtos.FindEmployeesDTO;
import com.sa.employee_service.employees.application.inputports.FindAllEmployeesInputPort;
import com.sa.employee_service.employees.application.outputports.FindAllEmployeesOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeesOutputPort;
import com.sa.employee_service.employees.domain.Employee;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllEmployeesUseCase implements FindAllEmployeesInputPort {

    private final FindEmployeesOutputPort findEmployeesOutputPort;

    @Override
    public List<Employee> handle(String employeeTypeId, String establishmentId) {
        UUID employeeTypeUuid = null;
        UUID establishmentUuid = null;
        if (employeeTypeId != null) {
            employeeTypeUuid = UUID.fromString(employeeTypeId);
        }
        if (establishmentId != null) {
            establishmentUuid = UUID.fromString(establishmentId);
        }

        FindEmployeesDTO filter = FindEmployeesDTO.builder()
            .employeeTypeId(employeeTypeUuid)
            .establishmentId(establishmentUuid)
            .build();


        return findEmployeesOutputPort.findEmployees(filter);
    }
}
