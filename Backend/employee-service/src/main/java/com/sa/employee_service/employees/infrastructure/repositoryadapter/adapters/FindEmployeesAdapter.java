package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.sa.employee_service.employees.application.dtos.FindEmployeesDTO;
import com.sa.employee_service.employees.application.outputports.FindEmployeesOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.EmployeeRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindEmployeesAdapter implements FindEmployeesOutputPort {

    private final EmployeeRepository employeeRepository;
    private final EmployeeRepositoryMapper employeeRepositoryMapper;

    @Override
    public List<Employee> findEmployees(FindEmployeesDTO filter) {
        Specification<EmployeeEntity> spec = Specification.where(null);

        if (filter.getEmployeeTypeId() != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("employeeType").get("id"), filter.getEmployeeTypeId().toString()));
        }

        if (filter.getEstablishmentId() != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("establishmentId"), filter.getEstablishmentId()));
        }

        return employeeRepository.findAll(spec)
            .stream()
            .map(employeeRepositoryMapper::toDomain)
            .toList();
    }

}
