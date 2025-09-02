package com.sa.employee_service.users.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.employee_service.users.application.inputports.FindUserByUsernameInputPort;
import com.sa.employee_service.users.application.outputports.FindUserByUsernameOutputPort;
import com.sa.employee_service.users.domain.User;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindUserByUsernameUseCase implements FindUserByUsernameInputPort {
    private final FindUserByUsernameOutputPort findUserByUsernameOutputPort;

    @Override
    public User handle(String username) throws NotFoundException {
        return findUserByUsernameOutputPort.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }
}
