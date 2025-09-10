package com.sa.employee_service.employees.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.inputports.FindAllEmployeeTypesInputPort;
import com.sa.employee_service.employees.application.outputports.FindAllEmployeeTypesOutpurPort;
import com.sa.employee_service.employees.domain.EmployeeType;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllEmployeeTypesUseCase implements FindAllEmployeeTypesInputPort {

    private final FindAllEmployeeTypesOutpurPort findAllEmployeeTypesOutpurPort;

    @Override
    public List<EmployeeType> handle() {
        return findAllEmployeeTypesOutpurPort.findAll();
    }

}
