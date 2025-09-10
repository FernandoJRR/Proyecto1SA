package com.sa.employee_service.employees.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.application.outputports.FindEmployeeTypeByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeesByTypeOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class FindEmployeesByTypeUseCaseTest {

    @Mock private FindEmployeesByTypeOutputPort findEmployeesByTypeOutputPort;
    @Mock private FindEmployeeTypeByIdOutputPort findEmployeeTypeByIdOutputPort;

    private FindEmployeesByTypeUseCase useCase;

    private static final String EMPLOYEE_TYPE_ID = "967c5c2b-ded0-4f0f-a68a-9466ac9e32f4";
    private static final UUID ESTABLISHMENT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @BeforeEach
    void setup() {
        useCase = new FindEmployeesByTypeUseCase(findEmployeesByTypeOutputPort, findEmployeeTypeByIdOutputPort);
    }

    // given: employee type exists
    // when: handle is invoked
    // then: delegates to output port and returns employees
    @Test
    void givenTypeExists_whenHandle_thenReturnsEmployees() throws Exception {
        // given
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(UUID.fromString(EMPLOYEE_TYPE_ID)))
            .thenReturn(Optional.of(new EmployeeType(UUID.fromString(EMPLOYEE_TYPE_ID), "Waiter")));

        List<Employee> expected = new ArrayList<>();
        expected.add(new Employee("cui1", "A", "B", new BigDecimal("1000")));
        expected.add(new Employee("cui2", "C", "D", new BigDecimal("1100")));
        when(findEmployeesByTypeOutputPort.findEmployeesByType(EMPLOYEE_TYPE_ID)).thenReturn(expected);

        // when
        List<Employee> result = useCase.handle(EMPLOYEE_TYPE_ID, ESTABLISHMENT_ID);

        // then
        assertEquals(expected, result);
        verify(findEmployeeTypeByIdOutputPort, times(1)).findEmployeeTypeById(UUID.fromString(EMPLOYEE_TYPE_ID));
        verify(findEmployeesByTypeOutputPort, times(1)).findEmployeesByType(EMPLOYEE_TYPE_ID);
    }

    // given: invalid employee type id format
    // when: handle is invoked
    // then: throws IllegalArgumentException and does not call ports
    @Test
    void givenInvalidTypeId_whenHandle_thenThrows() {
        // when - then
        assertThrows(IllegalArgumentException.class, () -> useCase.handle("bad-uuid", ESTABLISHMENT_ID));
        verify(findEmployeeTypeByIdOutputPort, never()).findEmployeeTypeById(UUID.randomUUID());
        verify(findEmployeesByTypeOutputPort, never()).findEmployeesByType(anyString());
    }

    // given: type id does not exist
    // when: handle is invoked
    // then: throws NotFoundException and does not call finder port
    @Test
    void givenMissingType_whenHandle_thenThrowsNotFound() {
        // given
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(UUID.fromString(EMPLOYEE_TYPE_ID)))
            .thenReturn(Optional.empty());

        // when - then
        assertThrows(NotFoundException.class, () -> useCase.handle(EMPLOYEE_TYPE_ID, ESTABLISHMENT_ID));
        verify(findEmployeesByTypeOutputPort, never()).findEmployeesByType(anyString());
    }
}
