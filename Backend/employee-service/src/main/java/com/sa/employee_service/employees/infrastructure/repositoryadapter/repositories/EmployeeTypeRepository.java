package com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeTypeEntity, String> {
    /**
     * Verifica si el nombre del tipo de empleado existe en la bd
     *
     * @param name
     * @return
     */
    public boolean existsByName(String name);

    public Optional<EmployeeTypeEntity> findByName(String name);

    public boolean existsByNameAndIdIsNot(String name, String id);

    public long deleteEmployeeTypeById(String employeeTypeId);
}
