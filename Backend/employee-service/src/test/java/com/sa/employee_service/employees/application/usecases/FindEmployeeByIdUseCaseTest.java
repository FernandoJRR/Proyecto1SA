package com.sa.employee_service.employees.application.usecases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.application.outputports.FindEmployeeByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class FindEmployeeByIdUseCaseTest {

    @Mock
    private FindEmployeeByIdOutputPort findEmployeeByIdOutputPort;

    private FindEmployeeByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new FindEmployeeByIdUseCase(findEmployeeByIdOutputPort);
    }

    @Test
    void handle_whenEmployeeExists_returnsEmployee() throws NotFoundException {
        // Arrange
        String id = UUID.randomUUID().toString();
        // Avoid relying on a specific constructor: mock the domain object
        Employee expected = mock(Employee.class);

        when(findEmployeeByIdOutputPort.findEmployeeById(id))
            .thenReturn(Optional.of(expected));

        // Act
        Employee result = useCase.handle(id);

        // Assert
        assertNotNull(result);
        assertSame(expected, result);
        // Optionally verify that output port is called once
        verify(findEmployeeByIdOutputPort, times(1)).findEmployeeById(id);
    }

    @Test
    void handle_whenEmployeeDoesNotExist_throwsNotFoundException() {
        // Arrange
        String id = UUID.randomUUID().toString();

        when(findEmployeeByIdOutputPort.findEmployeeById(id))
            .thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            useCase.handle(id);
        });

        assertEquals("Empleado no encontrado", ex.getMessage());
        verify(findEmployeeByIdOutputPort, times(1)).findEmployeeById(id);
    }
}