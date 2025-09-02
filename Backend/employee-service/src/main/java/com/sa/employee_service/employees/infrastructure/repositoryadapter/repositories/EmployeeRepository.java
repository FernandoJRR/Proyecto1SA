package com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, String>, JpaSpecificationExecutor<EmployeeEntity> {

    public Optional<EmployeeEntity> findByUser_Username(String username);
    public List<EmployeeEntity> findAllByEmployeeType_Id(String employeeTypeId);
}
