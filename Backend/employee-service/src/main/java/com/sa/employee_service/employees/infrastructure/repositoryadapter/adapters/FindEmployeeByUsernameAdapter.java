package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import com.sa.employee_service.employees.application.outputports.FindEmployeeByUsernameOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindEmployeeByUsernameAdapter implements FindEmployeeByUsernameOutputPort {
    private final EmployeeRepository employeeRepository;
    private final EmployeeRepositoryMapper employeeRepositoryMapper;

    public Optional<Employee> findEmployeeByUsername(String username) {
        return employeeRepository.findByUser_Username(username)
            .map(employeeRepositoryMapper::toDomain);
    }
}
