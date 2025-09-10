package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.employees.application.outputports.FindAllEmployeeTypesOutpurPort;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeTypeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeTypeRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindAllEmployeeTypesAdapter implements FindAllEmployeeTypesOutpurPort {

    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeTypeRepositoryMapper employeeTypeRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeType> findAll() {
        List<EmployeeTypeEntity> entities = employeeTypeRepository.findAll();
        return employeeTypeRepositoryMapper.toDomain(entities);
    }

}
