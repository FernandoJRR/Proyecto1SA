package com.sa.employee_service.employees.application.usecases;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.application.dtos.UpdateEmployeeDTO;
import com.sa.employee_service.employees.application.outputports.FindEmployeeByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.UpdateEmployeeOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class UpdateEmployeeUseCaseTest {

    @Mock private FindEmployeeByIdOutputPort findEmployeeByIdOutputPort;
    @Mock private UpdateEmployeeOutputPort updateEmployeeOutputPort;

    private UpdateEmployeeUseCase useCase;

    private static final String EMPLOYEE_ID = UUID.randomUUID().toString();

    @BeforeEach
    void setup() {
        useCase = new UpdateEmployeeUseCase(findEmployeeByIdOutputPort, updateEmployeeOutputPort);
    }

    // given: existing employee and valid update dto
    // when: handle is invoked
    // then: updates fields and persists via output port
    @Test
    void givenExistingEmployee_whenHandle_thenUpdatesAndPersists() throws Exception {
        // given existing
        Employee existing = new Employee("1234567890123", "Michael", "Kane", new BigDecimal("1200.00"));
        existing.setId(UUID.fromString(EMPLOYEE_ID));
        when(findEmployeeByIdOutputPort.findEmployeeById(EMPLOYEE_ID)).thenReturn(Optional.of(existing));

        UpdateEmployeeDTO dto = new UpdateEmployeeDTO();
        // reflection-free: use fields via package-private setters are not present; use direct field assignment not available.
        // Build via small helper
        set(dto, "Carlos", "Ramirez", new BigDecimal("7000.00"));

        // echo back updated
        when(updateEmployeeOutputPort.updateEmployee(same(existing))).thenAnswer(inv -> inv.getArgument(0));

        // when
        Employee result = useCase.handle(EMPLOYEE_ID, dto);

        // then
        assertAll(
            () -> assertEquals("Carlos", result.getFirstName()),
            () -> assertEquals("Ramirez", result.getLastName()),
            () -> assertEquals(new BigDecimal("7000.00"), result.getSalary())
        );
        verify(findEmployeeByIdOutputPort, times(1)).findEmployeeById(EMPLOYEE_ID);
        verify(updateEmployeeOutputPort, times(1)).updateEmployee(same(existing));
    }

    // given: employee does not exist
    // when: handle is invoked
    // then: throws NotFoundException and does not call output port
    @Test
    void givenMissingEmployee_whenHandle_thenThrowsNotFound() {
        when(findEmployeeByIdOutputPort.findEmployeeById(EMPLOYEE_ID)).thenReturn(Optional.empty());

        UpdateEmployeeDTO dto = new UpdateEmployeeDTO();
        set(dto, "Carlos", "Ramirez", new BigDecimal("7000.00"));

        assertThrows(NotFoundException.class, () -> useCase.handle(EMPLOYEE_ID, dto));
    }

    private static void set(UpdateEmployeeDTO dto, String first, String last, BigDecimal salary) {
        try {
            java.lang.reflect.Field f1 = UpdateEmployeeDTO.class.getDeclaredField("firstName");
            java.lang.reflect.Field f2 = UpdateEmployeeDTO.class.getDeclaredField("lastName");
            java.lang.reflect.Field f3 = UpdateEmployeeDTO.class.getDeclaredField("salary");
            f1.setAccessible(true); f2.setAccessible(true); f3.setAccessible(true);
            f1.set(dto, first); f2.set(dto, last); f3.set(dto, salary);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

