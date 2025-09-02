package com.sa.employee_service.auth.application.inputports;

import org.springframework.security.authentication.BadCredentialsException;

import com.sa.employee_service.auth.application.dtos.LoginDTO;
import com.sa.shared.exceptions.*;
public interface LoginInputPort {
    /**
     * Autentica al usuario con sus credenciales.
     *
     * @param username
     * @param password
     * @return
     * @throws NotFoundException
     * @throws BadCredentialsException
     */
    public LoginDTO login(String username, String password) throws NotFoundException, BadCredentialsException;
}
