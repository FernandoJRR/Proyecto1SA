package com.sa.employee_service.employees.application.usecases;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.application.inputports.CreateEmployeeInputPort;
import com.sa.employee_service.employees.application.outputports.CreateEmployeeOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeTypeByIdOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.users.application.inputports.CreateUserInputPort;
import com.sa.employee_service.users.application.services.CreateUserService;
import com.sa.employee_service.users.domain.User;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
@Validated
public class CreateEmployeeUseCase implements CreateEmployeeInputPort {
    private final CreateEmployeeOutputPort createEmployeeOutputPort;
    private final FindEmployeeTypeByIdOutputPort findEmployeeTypeByIdOutputPort;
    private final CreateUserService createUserService;

    @Transactional
    public Employee handle(@Valid CreateEmployeeDTO createEmployeeDTO) throws DuplicatedEntryException, NotFoundException {
        Optional<EmployeeType> foundEmployeeType = findEmployeeTypeByIdOutputPort.findEmployeeTypeById(UUID.fromString(createEmployeeDTO.getEmployeeTypeId().getId()));
        if (foundEmployeeType.isEmpty()) {
            throw new NotFoundException("El tipo de empleado no fue encontrado");
        }

        User createdUser = createUserService.createUser(createEmployeeDTO.getCreateUserDTO());

        Employee emp = Employee.hire(
                createEmployeeDTO.getCui(),
                createEmployeeDTO.getFirstName(),
                createEmployeeDTO.getLastName(),
                createEmployeeDTO.getSalary(),
                foundEmployeeType.get(),
                createEmployeeDTO.getHiredAt(),
                createEmployeeDTO.getEstablishmentId()
        );
        return createEmployeeOutputPort.createEmployee(createdUser.getId().toString(), emp);
    }
}
