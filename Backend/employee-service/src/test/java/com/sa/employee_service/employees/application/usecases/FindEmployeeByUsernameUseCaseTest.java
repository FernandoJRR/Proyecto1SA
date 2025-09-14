package com.sa.employee_service.employees.application.usecases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.application.outputports.FindEmployeeByUsernameOutputPort;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class FindEmployeeByUsernameUseCaseTest {

    @Mock
    private FindEmployeeByUsernameOutputPort findEmployeeByUsernameOutputPort;

    private FindEmployeeByUsernameUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new FindEmployeeByUsernameUseCase(findEmployeeByUsernameOutputPort);
    }

    @Test
    void handle_whenEmployeeExists_returnsEmployee() throws NotFoundException {
        // Arrange
        String username = "user123";
        Employee expected = mock(Employee.class);

        when(findEmployeeByUsernameOutputPort.findEmployeeByUsername(username))
            .thenReturn(Optional.of(expected));

        // Act
        Employee result = useCase.handle(username);

        // Assert
        assertNotNull(result);
        assertSame(expected, result);
        verify(findEmployeeByUsernameOutputPort, times(1)).findEmployeeByUsername(username);
    }

    @Test
    void handle_whenEmployeeDoesNotExist_throwsNotFoundException() {
        // Arrange
        String username = "nonexistent";

        when(findEmployeeByUsernameOutputPort.findEmployeeByUsername(username))
            .thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            useCase.handle(username);
        });

        assertEquals("Empleado no encontrado", ex.getMessage());
        verify(findEmployeeByUsernameOutputPort, times(1)).findEmployeeByUsername(username);
    }
}