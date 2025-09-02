package com.sa.employee_service.employees.application.usecases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.application.inputports.ForEmployeeTypePort;
import com.sa.employee_service.employees.application.inputports.ForEmployeesPort;
import com.sa.employee_service.employees.application.specifications.EmployeeSpecifications;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeHistory;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.HistoryType;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;
import com.sa.employee_service.employees.infrastructure.restadapter.dtos.EmployeeRequestDTO;
import com.sa.employee_service.shared.domain.enums.EmployeeTypeEnum;
import com.sa.employee_service.users.application.inputports.ForUsersPort;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.InvalidPeriodException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class EmployeeService implements ForEmployeesPort {

    private final EmployeeRepository employeeRepository;
    private final ForEmployeeTypePort forEmployeeTypePort;

    @Override
    public EmployeeEntity updateEmployee(String currentId, EmployeeEntity newData, EmployeeTypeEntity employeeType)
            throws NotFoundException {
        // traer el empleado por id
        EmployeeEntity currentEmployee = findEmployeeById(currentId);

        // traer el tipo de empleado que se desea asignar
        EmployeeTypeEntity existingEmployeeType = forEmployeeTypePort.findEmployeeTypeById(employeeType.getId());

        // editar el empleado existente con la información de newData
        currentEmployee.setCui(newData.getCui());
        currentEmployee.setFirstName(newData.getFirstName());
        currentEmployee.setLastName(newData.getLastName());
        currentEmployee.setSalary(newData.getSalary());
        currentEmployee.setEmployeeType(existingEmployeeType);

        return employeeRepository.save(currentEmployee);
    }

    @Override
    public EmployeeEntity updateEmployeeSalary(String currentId, BigDecimal newSalary, LocalDate salaryDate)
            throws NotFoundException, InvalidPeriodException {
        EmployeeEntity currentEmployee = findEmployeeById(currentId);

        currentEmployee.setSalary(newSalary);

        return employeeRepository.save(currentEmployee);
    }

    @Override
    public EmployeeEntity desactivateEmployee(String currentId, LocalDate deactivationDate, HistoryType historyTypeReason)
            throws NotFoundException, IllegalStateException, InvalidPeriodException {
        // traer el empleado por id
        EmployeeEntity currentEmployee = findEmployeeById(currentId);

        // si ya esta desactivado entonces lanzamos error
        if (currentEmployee.getDesactivatedAt() != null) {
            // indicamos que se llamo el metodo en un momento inapropiado
            throw new IllegalStateException("El empleado ya está desactivado.");
        }

        // le cambiamos el estado a su usuario y al empleado
        LocalDate desactivatedDate = deactivationDate;
        currentEmployee.setDesactivatedAt(desactivatedDate);
        currentEmployee.getUser().setDesactivatedAt(desactivatedDate);

        return employeeRepository.save(currentEmployee);
    }

    @Override
    public EmployeeEntity reactivateEmployee(String currentId, LocalDate reactivationDate)
            throws NotFoundException, IllegalStateException, InvalidPeriodException {
        // traer el empleado por id
        EmployeeEntity currentEmployee = findEmployeeById(currentId);

        // si ya esta desactivado entonces lanzamos error
        if (currentEmployee.getDesactivatedAt() == null) {
            // indicamos que se llamo el metodo en un momento inapropiado
            throw new IllegalStateException("El empleado esta activado.");
        }

        // le cambiamos el estado a su usuario y al empleado
        currentEmployee.setDesactivatedAt(null);
        currentEmployee.getUser().setDesactivatedAt(null);

        return employeeRepository.save(currentEmployee);
    }

    @Override
    public EmployeeEntity reassignEmployeeType(String employeeId, String employeeTypeId) throws NotFoundException {
        EmployeeEntity exisitingEmployee = findEmployeeById(employeeId);
        EmployeeTypeEntity existinEmployeeType = forEmployeeTypePort.findEmployeeTypeById(employeeTypeId);
        exisitingEmployee.setEmployeeType(existinEmployeeType);
        return exisitingEmployee;
    }

    @Override
    public List<EmployeeEntity> reassignEmployeeType(List<EmployeeEntity> employeeIds, String employeeTypeId)
            throws NotFoundException {
        List<EmployeeEntity> updatedEmployees = new ArrayList<>();
        for (EmployeeEntity employeeId : employeeIds) {
            EmployeeEntity reassignEmployeeType = reassignEmployeeType(employeeId.getId(), employeeTypeId);
            updatedEmployees.add(reassignEmployeeType);
        }
        return updatedEmployees;
    }

    @Override
    public EmployeeEntity findEmployeeById(String employeeId) throws NotFoundException {
        String errorMessage = String.format("El id %s no pertenece a ningun empleado.", employeeId);
        // manda a traer el employee si el optional esta vacio entonces retorna un
        // notfound exception
        EmployeeEntity employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException(errorMessage));

        return employee;
    }

    @Override
    public EmployeeEntity findEmployeeByUsername(String username) throws NotFoundException {
        String errorMessage = String.format("El nombre de usuario %s no pertenece a ningun empleado.", username);
        // manda a traer el employee si el optional esta vacio entonces retorna un
        // notfound exception
        EmployeeEntity employee = employeeRepository.findByUser_Username(username).orElseThrow(
                () -> new NotFoundException(errorMessage));

        return employee;
    }

    @Override
    public List<EmployeeEntity> findEmployees() {
        // manda a traer el employee si el optional esta vacio entonces retorna un
        // notfound exception
        List<EmployeeEntity> employees = employeeRepository.findAll();

        return employees;
    }

    @Override
    public List<EmployeeEntity> getEmployeesByType(String employeeTypeId, String search) throws NotFoundException {
        // Verificamos si existe el tipo de empleado
        EmployeeTypeEntity employeeType = forEmployeeTypePort.findEmployeeTypeById(employeeTypeId);
        Specification<EmployeeEntity> spec = Specification
                .where(EmployeeSpecifications.hasEmployeeTypeId(employeeType.getId()))
                .and(EmployeeSpecifications.hasFirstName(search))
                .and(EmployeeSpecifications.hasLastName(search))
                .and(EmployeeSpecifications.isActive(true));
        List<EmployeeEntity> employees = employeeRepository.findAll(spec);
        return employees;
    }
}
