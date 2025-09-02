package com.sa.employee_service.employees.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.inputports.FindAllEmployeesInputPort;
import com.sa.employee_service.employees.application.outputports.FindAllEmployeesOutputPort;
import com.sa.employee_service.employees.domain.Employee;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllEmployeesUseCase implements FindAllEmployeesInputPort {
    private final FindAllEmployeesOutputPort findAllEmployeesOutputPort;
    public List<Employee> handle() {
        return findAllEmployeesOutputPort.findAllEmployees();
    }
}
