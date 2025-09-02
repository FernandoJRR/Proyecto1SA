package com.sa.employee_service.employees.application.inputports;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeHistory;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.HistoryType;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.EmployeeRequestDTO;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.shared.exceptions.*;

public interface ForEmployeesPort {

    public EmployeeEntity updateEmployee(String currentId, EmployeeEntity newData, EmployeeTypeEntity employeeType)
            throws NotFoundException;

    public EmployeeEntity updateEmployeeSalary(String currentId, BigDecimal newSalary, LocalDate salaryDate)
            throws NotFoundException, InvalidPeriodException;

    public EmployeeEntity reassignEmployeeType(String employeeId, String employeeTypeId) throws NotFoundException;

    public List<EmployeeEntity> reassignEmployeeType(List<EmployeeEntity> employeeIds, String employeeTypeId)
            throws NotFoundException;

    public EmployeeEntity findEmployeeById(String employeeId) throws NotFoundException;

    public EmployeeEntity findEmployeeByUsername(String username) throws NotFoundException;

    public List<EmployeeEntity> findEmployees();

    public EmployeeEntity desactivateEmployee(String currentId, LocalDate deactivationDate, HistoryType historyTypeReason)
            throws NotFoundException, IllegalStateException, InvalidPeriodException;

    public EmployeeEntity reactivateEmployee(String currentId, LocalDate deactivationDate)
            throws NotFoundException, IllegalStateException, InvalidPeriodException;

    public List<EmployeeEntity> getEmployeesByType(String employeeTypeId, String search) throws NotFoundException;
}
