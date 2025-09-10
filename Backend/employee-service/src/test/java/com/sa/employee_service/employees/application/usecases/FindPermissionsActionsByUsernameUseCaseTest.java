package com.sa.employee_service.employees.application.usecases;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.application.outputports.FindEmployeeByUsernameOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class FindPermissionsActionsByUsernameUseCaseTest {

    @Mock
    private FindEmployeeByUsernameOutputPort findEmployeeByUsernameOutputPort;

    private FindPermissionsActionsByUsernameUseCase useCase;

    private static final String USERNAME = "fernando";

    @BeforeEach
    void setup() {
        useCase = new FindPermissionsActionsByUsernameUseCase(findEmployeeByUsernameOutputPort);
    }

    // given: employee exists with permissions
    // when: handle is invoked
    // then: returns list of permission actions
    @Test
    void givenEmployeeWithPermissions_whenHandle_thenReturnsActions() throws Exception {
        // given
        EmployeeType type = new EmployeeType(UUID.randomUUID(), "Admin");
        type.setPermissions(Arrays.asList(
            Permission.create("Users", "USERS_READ"),
            Permission.create("Users", "USERS_WRITE"),
            Permission.create("Employees", "EMPLOYEES_READ")
        ));

        Employee employee = new Employee("1234567890123", "Michael", "Kane", new BigDecimal("1200"), type, LocalDate.now());
        when(findEmployeeByUsernameOutputPort.findEmployeeByUsername(USERNAME)).thenReturn(Optional.of(employee));

        // when
        List<String> actions = useCase.handle(USERNAME);

        // then
        assertAll(
            () -> assertEquals(3, actions.size()),
            () -> assertEquals("USERS_READ", actions.get(0)),
            () -> assertEquals("USERS_WRITE", actions.get(1)),
            () -> assertEquals("EMPLOYEES_READ", actions.get(2))
        );
        verify(findEmployeeByUsernameOutputPort).findEmployeeByUsername(USERNAME);
    }

    // given: employee exists with no permissions
    // when: handle is invoked
    // then: returns empty list
    @Test
    void givenEmployeeWithoutPermissions_whenHandle_thenReturnsEmptyList() throws Exception {
        // given
        EmployeeType type = new EmployeeType(UUID.randomUUID(), "Basic");
        type.setPermissions(List.of());
        Employee employee = new Employee("1234567890123", "Ana", "Lopez", new BigDecimal("900"), type, LocalDate.now());
        when(findEmployeeByUsernameOutputPort.findEmployeeByUsername(USERNAME)).thenReturn(Optional.of(employee));

        // when
        List<String> actions = useCase.handle(USERNAME);

        // then
        assertEquals(0, actions.size());
        verify(findEmployeeByUsernameOutputPort).findEmployeeByUsername(USERNAME);
    }

    // given: employee not found
    // when: handle is invoked
    // then: throws NotFoundException
    @Test
    void givenMissingEmployee_whenHandle_thenThrowsNotFound() {
        // given
        when(findEmployeeByUsernameOutputPort.findEmployeeByUsername(USERNAME)).thenReturn(Optional.empty());

        // when - then
        assertThrows(NotFoundException.class, () -> useCase.handle(USERNAME));
        verify(findEmployeeByUsernameOutputPort).findEmployeeByUsername(USERNAME);
    }
}
