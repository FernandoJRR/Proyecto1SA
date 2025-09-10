package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.employees.application.outputports.FindEmployeeTypeByIdOutputPort;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeTypeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeTypeRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindEmployeeTypeByIdAdapter implements FindEmployeeTypeByIdOutputPort {
    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeTypeRepositoryMapper employeeTypeRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeType> findEmployeeTypeById(UUID id) {
        Optional<EmployeeType> gotten = employeeTypeRepository.findById(id.toString())
            .map(employeeTypeRepositoryMapper::toDomain);
        return gotten;
    }
}
