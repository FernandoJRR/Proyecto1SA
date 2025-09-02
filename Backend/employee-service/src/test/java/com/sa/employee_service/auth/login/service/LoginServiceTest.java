package com.sa.employee_service.auth.login.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import com.sa.employee_service.auth.application.dtos.LoginDTO;
import com.sa.employee_service.auth.application.outputports.GenerateJWTTokenOutputPort;
import com.sa.employee_service.auth.application.usecases.LoginService;
import com.sa.employee_service.employees.application.inputports.FindEmployeeByUsernameInputPort;
import com.sa.employee_service.employees.application.inputports.FindPermissionsActionsByUsernameInputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.employee_service.shared.utils.PasswordEncoderUtil;
import com.sa.employee_service.users.application.usecases.FindUserByUsernameUseCase;
import com.sa.employee_service.users.domain.User;
import com.sa.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private GenerateJWTTokenOutputPort generateJWTTokenOutputPort;
    @Mock
    private FindPermissionsActionsByUsernameInputPort findPermissionsActionsByUsernameInputPort;
    @Mock
    private FindUserByUsernameUseCase findUserByUsernameUseCase;
    @Mock
    private FindEmployeeByUsernameInputPort findEmployeeByUsernameInputPort;
    @Mock
    private PasswordEncoderUtil passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "1234";
    private static final String HASHED_PASSWORD = "hashed1234";
    private static final String JWT = "jwt-token";

    UUID EMPLOYEE_UUID = UUID.randomUUID();
    UUID PERMISSION_UUID = UUID.randomUUID();
    UUID EMPLOYEE_TYPE_UUID = UUID.randomUUID();

    private User user;
    private Employee employee;

    @BeforeEach
    void setUp() {
        user = org.mockito.Mockito.mock(User.class);
        employee = new Employee(EMPLOYEE_UUID);

        // Mock de permisos y tipo de empleado
        Permission permission = new Permission(PERMISSION_UUID, "READ", "READ");

        EmployeeType employeeType = new EmployeeType(EMPLOYEE_TYPE_UUID, "ADMIN");
        employeeType.setPermissions(List.of(permission));

        employee.setEmployeeType(employeeType);
    }

    /**
     * dado: credenciales válidas y usuario activo.
     * cuando: se llama a login.
     * entonces: se retorna correctamente el LoginResponseDTO con JWT.
     */
    @Test
    public void loginShouldSucceedWithValidCredentials() throws NotFoundException {
        when(user.getUsername()).thenReturn(USERNAME);
        when(user.getPassword()).thenReturn(HASHED_PASSWORD);
        when(user.getDesactivatedAt()).thenReturn(null);

        when(findUserByUsernameUseCase.handle(USERNAME)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(findPermissionsActionsByUsernameInputPort.handle(USERNAME)).thenReturn(List.of("READ"));
        when(generateJWTTokenOutputPort.generateToken(user, List.of("READ"))).thenReturn(JWT);

        when(findEmployeeByUsernameInputPort.handle(USERNAME)).thenReturn(employee);

        LoginDTO result = loginService.login(USERNAME, PASSWORD);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
        assertEquals(JWT, result.getToken());
        assertEquals(employee, result.getEmployee());
        assertEquals(employee.getEmployeeType().getPermissions(), result.getPermissions());
    }

    /**
     * dado: contraseña incorrecta.
     * cuando: se llama a login.
     * entonces: se lanza BadCredentialsException.
     */
    @Test
    public void loginShouldThrowWhenPasswordInvalid() throws NotFoundException {
        when(user.getPassword()).thenReturn(HASHED_PASSWORD);

        when(findUserByUsernameUseCase.handle(USERNAME)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> loginService.login(USERNAME, PASSWORD));
    }

    /**
     * dado: usuario desactivado.
     * cuando: se llama a login.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void loginShouldThrowWhenUserIsDeactivated() throws NotFoundException {
        when(user.getPassword()).thenReturn(HASHED_PASSWORD);
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(user.getDesactivatedAt()).thenReturn(LocalDate.now());
        when(findUserByUsernameUseCase.handle(USERNAME)).thenReturn(user);

        assertThrows(NotFoundException.class, () -> loginService.login(USERNAME, PASSWORD));
    }

    /**
     * dado: nombre de usuario no existe.
     * cuando: se llama a login.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void loginShouldThrowWhenUserNotFound() throws NotFoundException {
        when(findUserByUsernameUseCase.handle(USERNAME)).thenThrow(new NotFoundException("no existe"));

        assertThrows(NotFoundException.class, () -> loginService.login(USERNAME, PASSWORD));
    }
}
