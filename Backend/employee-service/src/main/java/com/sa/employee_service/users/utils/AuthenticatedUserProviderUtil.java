package com.sa.employee_service.users.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sa.employee_service.employees.application.inputports.ForEmployeesPort;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.users.application.inputports.AuthenticationProviderPort;
import com.sa.shared.exceptions.*;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserProviderUtil implements AuthenticationProviderPort {
    private final ForEmployeesPort forEmployeesPort;

    @Override
    public EmployeeEntity getAutenticatedEmployee() throws IllegalStateException, NotFoundException {
        // traemos el nobre de usuairo del contexto de seguridad
        String userName = getUsername();
        // usamos el servicio para cargar el empeado
        EmployeeEntity employee = forEmployeesPort.findEmployeeByUsername(userName);
        return employee;
    }

    /**
     * Obtiene el nombre de usuario del contexto de seguridad actual.
     *
     * @return el nombre de usuario autenticado.
     * @throws IllegalStateException si no hay autenticación presente en el contexto
     *                               de seguridad.
     */
    private String getUsername() throws IllegalStateException {
        // mandamos a traer el contexto de seguridad de spirng
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // si esta nulo entonces mandamos a lanzar una ilegalState
        if (authentication == null) {
            throw new IllegalStateException("No hay ningun usuario autenticado.");
        }
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }
}
