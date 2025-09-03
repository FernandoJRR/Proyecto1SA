package com.sa.employee_service.employees.application.inputports;

import com.sa.shared.exceptions.InvalidParameterException;

import org.springframework.validation.annotation.Validated;

import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface CreateEmployeeInputPort {
    public Employee handle(@Valid CreateEmployeeDTO createEmployeeDTO) throws DuplicatedEntryException, NotFoundException, InvalidParameterException;
}
