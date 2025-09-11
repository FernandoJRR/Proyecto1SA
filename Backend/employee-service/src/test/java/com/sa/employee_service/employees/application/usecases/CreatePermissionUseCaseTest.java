package com.sa.employee_service.employees.application.usecases;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.application.dtos.CreatePermissionDTO;
import com.sa.employee_service.employees.application.outputports.CreatePermissionOutputPort;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.shared.exceptions.DuplicatedEntryException;

@ExtendWith(MockitoExtension.class)
public class CreatePermissionUseCaseTest {

    @Mock private CreatePermissionOutputPort createPermissionOutputPort;

    private CreatePermissionUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new CreatePermissionUseCase(createPermissionOutputPort);
    }

    // given: valid name and action
    // when: handle is invoked
    // then: creates Permission, passes to output port, and returns result
    @Test
    void givenValidInput_whenHandle_thenCreatesAndDelegates() throws DuplicatedEntryException {
        // given
        CreatePermissionDTO dto = new CreatePermissionDTO("Usuarios", "USERS_READ");

        // Stub output to echo the same permission back
        ArgumentCaptor<Permission> captor = ArgumentCaptor.forClass(Permission.class);
        when(createPermissionOutputPort.createPermission(captor.capture()))
            .thenAnswer(inv -> inv.getArgument(0));

        // when
        Permission result = useCase.handle(dto);

        // then
        Permission sent = captor.getValue();
        assertAll(
            () -> assertNotNull(sent.getId(), "Permission id should be generated"),
            () -> assertEquals("Usuarios", sent.getName()),
            () -> assertEquals("USERS_READ", sent.getAction()),
            () -> assertNotNull(result, "Returned permission should not be null"),
            () -> assertEquals(sent.getId(), result.getId())
        );
        verify(createPermissionOutputPort, times(1)).createPermission(sent);
    }
}

