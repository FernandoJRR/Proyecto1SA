package com.sa.employee_service.auth.application.usecases;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.sa.employee_service.auth.application.dtos.LoginDTO;
import com.sa.employee_service.auth.application.inputports.LoginInputPort;
import com.sa.employee_service.auth.application.outputports.GenerateJWTTokenOutputPort;
import com.sa.employee_service.auth.infrastructure.restadapter.dtos.LoginResponse;
import com.sa.employee_service.employees.application.inputports.FindEmployeeByUsernameInputPort;
import com.sa.employee_service.employees.application.inputports.FindPermissionsActionsByUsernameInputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.infrastructure.restadapter.mappers.EmployeeMapper;
import com.sa.employee_service.employees.infrastructure.restadapter.mappers.PermissionMapper;
import com.sa.employee_service.shared.utils.PasswordEncoderUtil;
import com.sa.employee_service.users.application.inputports.FindUserByUsernameInputPort;
import com.sa.employee_service.users.application.inputports.ForUsersPort;
import com.sa.employee_service.users.application.usecases.FindUserByUsernameUseCase;
import com.sa.employee_service.users.domain.User;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class LoginUseCase implements LoginInputPort {

    private final FindUserByUsernameUseCase findUserByUsernameUseCase;
    private final FindEmployeeByUsernameInputPort findEmployeeByUsernameInputPort;

    private final GenerateJWTTokenOutputPort forJwtGenerator;
    private final FindPermissionsActionsByUsernameInputPort findPermissionsActionsByUsernameInputPort;

    private final PasswordEncoderUtil passwordEncoder;

    @Override
    public LoginDTO login(String username, String password) throws NotFoundException, BadCredentialsException {

        User user = findUserByUsernameUseCase.handle(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("");
        }

        if (user.getDesactivatedAt() != null) {
            throw new NotFoundException("El usuario se encuentra desactivado.");
        }

        // si la autenticacion no fallo entonces cargamos los permisos del usuario
        List<String> authorities = findPermissionsActionsByUsernameInputPort.handle(username);

        // cagados los permisos entonces generamos el jwt
        String jwt = forJwtGenerator.generateToken(user, authorities);

        // construimos la respuesta
        Employee employee = findEmployeeByUsernameInputPort.handle(username);

        return new LoginDTO(
            user.getUsername(),
            employee,
            jwt,
            employee.getEmployeeType().getPermissions()
        );

    }

}
