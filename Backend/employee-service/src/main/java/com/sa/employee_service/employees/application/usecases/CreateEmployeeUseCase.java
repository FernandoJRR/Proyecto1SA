package com.sa.employee_service.employees.application.usecases;

import com.sa.shared.exceptions.InvalidParameterException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.application.dtos.FindEmployeesDTO;
import com.sa.employee_service.employees.application.inputports.CreateEmployeeInputPort;
import com.sa.employee_service.employees.application.outputports.CreateEmployeeOutputPort;
import com.sa.employee_service.employees.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.ExistsRestaurantByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeTypeByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeesOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.domain.EmployeeType;
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
    private final FindEmployeesOutputPort findEmployeesOutputPort;
    private final CreateUserService createUserService;
    private final ExistsHotelByIdOutputPort existsHotelByIdOutputPort;
    private final ExistsRestaurantByIdOutputPort existsRestaurantByIdOutputPort;

    @Transactional
    public Employee handle(@Valid CreateEmployeeDTO createEmployeeDTO)
            throws DuplicatedEntryException, NotFoundException, InvalidParameterException {
        Optional<EmployeeType> foundEmployeeType = findEmployeeTypeByIdOutputPort
                .findEmployeeTypeById(UUID.fromString(createEmployeeDTO.getEmployeeTypeId().getId()));
        if (foundEmployeeType.isEmpty()) {
            throw new NotFoundException("El tipo de empleado no fue encontrado");
        }

        List<Employee> foundSameCui = findEmployeesOutputPort.findEmployees(FindEmployeesDTO.builder()
                .cui(createEmployeeDTO.getCui()).build());
        if (!foundSameCui.isEmpty()) {
            throw new DuplicatedEntryException("Ya existe un empleado con el mismo CUI");
        }

        if (createEmployeeDTO.getEstablishmentId() != null) {
            if (createEmployeeDTO.getEstablishmentType() == null) {
                throw new InvalidParameterException(
                        "Si un ID de establecimiento es ingresado, debe de existir su tipo tambien");
            }

            if (createEmployeeDTO.getEstablishmentType().equals("HOTEL")) {
                if (!existsHotelByIdOutputPort.existsById(createEmployeeDTO.getEstablishmentId().toString())) {
                    throw new NotFoundException("El hotel buscado no existe");
                }
            } else if (createEmployeeDTO.getEstablishmentType().equals("RESTAURANT")) {
                if (!existsRestaurantByIdOutputPort.existsById(createEmployeeDTO.getEstablishmentId().toString())) {
                    throw new NotFoundException("El restaurante buscado no existe");
                }
            }
        }

        User createdUser = createUserService.createUser(createEmployeeDTO.getCreateUserDTO());

        Employee emp = Employee.hire(
                createEmployeeDTO.getCui(),
                createEmployeeDTO.getFirstName(),
                createEmployeeDTO.getLastName(),
                createEmployeeDTO.getSalary(),
                foundEmployeeType.get(),
                createEmployeeDTO.getHiredAt(),
                createEmployeeDTO.getEstablishmentId(),
                createEmployeeDTO.getEstablishmentType());
        return createEmployeeOutputPort.createEmployee(createdUser.getId().toString(), emp);
    }
}
