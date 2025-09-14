package com.sa.employee_service.employees.application.usecases;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.application.dtos.FindEmployeesDTO;
import com.sa.employee_service.employees.application.outputports.FindEmployeesOutputPort;
import com.sa.employee_service.employees.domain.Employee;

@ExtendWith(MockitoExtension.class)
public class FindAllEmployeesUseCaseTest {

    @Mock
    private FindEmployeesOutputPort findEmployeesOutputPort;

    private FindAllEmployeesUseCase useCase;

    private static final String EMPLOYEE_TYPE_ID = "967c5c2b-ded0-4f0f-a68a-9466ac9e32f4";
    private static final String ESTABLISHMENT_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setup() {
        useCase = new FindAllEmployeesUseCase(findEmployeesOutputPort);
    }

    // given: no filters (both ids null)
    // when: handle is invoked
    // then: forwards nulls to output port and returns its result
    @Test
    void givenNoFilters_whenHandle_thenReturnsAllFromPort() {
        // given
        List<Employee> expected = sampleEmployees(2);
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class))).thenReturn(expected);

        // when
        List<Employee> result = useCase.handle(null, null);

        // then
        ArgumentCaptor<FindEmployeesDTO> captor = ArgumentCaptor.forClass(FindEmployeesDTO.class);
        verify(findEmployeesOutputPort, times(1)).findEmployees(captor.capture());
        FindEmployeesDTO sent = captor.getValue();

        assertAll(
            () -> assertEquals(expected, result),
            () -> assertEquals(null, sent.getEmployeeTypeId()),
            () -> assertEquals(null, sent.getEstablishmentId())
        );
    }

    // given: both employeeTypeId and establishmentId are provided
    // when: handle is invoked
    // then: parses UUIDs and forwards filter to output port
    @Test
    void givenBothFilters_whenHandle_thenParsesAndForwards() {
        // given
        List<Employee> expected = sampleEmployees(1);
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class))).thenReturn(expected);

        // when
        List<Employee> result = useCase.handle(EMPLOYEE_TYPE_ID, ESTABLISHMENT_ID);

        // then
        ArgumentCaptor<FindEmployeesDTO> captor = ArgumentCaptor.forClass(FindEmployeesDTO.class);
        verify(findEmployeesOutputPort, times(1)).findEmployees(captor.capture());
        FindEmployeesDTO sent = captor.getValue();

        assertAll(
            () -> assertEquals(expected, result),
            () -> assertEquals(UUID.fromString(EMPLOYEE_TYPE_ID), sent.getEmployeeTypeId()),
            () -> assertEquals(UUID.fromString(ESTABLISHMENT_ID), sent.getEstablishmentId())
        );
    }

    // given: only employeeTypeId is provided
    // when: handle is invoked
    // then: forwards employeeTypeId and null establishmentId
    @Test
    void givenOnlyEmployeeType_whenHandle_thenForwardsEmployeeType() {
        // given
        List<Employee> expected = sampleEmployees(3);
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class))).thenReturn(expected);

        // when
        List<Employee> result = useCase.handle(EMPLOYEE_TYPE_ID, null);

        // then
        ArgumentCaptor<FindEmployeesDTO> captor = ArgumentCaptor.forClass(FindEmployeesDTO.class);
        verify(findEmployeesOutputPort, times(1)).findEmployees(captor.capture());
        FindEmployeesDTO sent = captor.getValue();

        assertAll(
            () -> assertEquals(expected, result),
            () -> assertEquals(UUID.fromString(EMPLOYEE_TYPE_ID), sent.getEmployeeTypeId()),
            () -> assertEquals(null, sent.getEstablishmentId())
        );
    }

    // given: invalid employeeTypeId format
    // when: handle is invoked
    // then: throws IllegalArgumentException and does not call port
    @Test
    void givenInvalidEmployeeType_whenHandle_thenThrows() {
        // when - then
        assertThrows(IllegalArgumentException.class, () -> useCase.handle("bad-uuid", null));
        verifyNoInteractions(findEmployeesOutputPort);
    }

    // given: invalid establishmentId format
    // when: handle is invoked
    // then: throws IllegalArgumentException and does not call port
    @Test
    void givenInvalidEstablishment_whenHandle_thenThrows() {
        // when - then
        assertThrows(IllegalArgumentException.class, () -> useCase.handle(null, "bad-uuid"));
        verifyNoInteractions(findEmployeesOutputPort);
    }

    private static List<Employee> sampleEmployees(int n) {
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new Employee("cui" + i, "Name" + i, "Last" + i, new BigDecimal("1000")));
        }
        return list;
    }
}
