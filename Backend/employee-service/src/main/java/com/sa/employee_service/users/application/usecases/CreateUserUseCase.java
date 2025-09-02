package com.sa.employee_service.users.application.usecases;

import org.springframework.stereotype.Service;

import com.sa.employee_service.shared.utils.PasswordEncoderUtil;
import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.application.inputports.CreateUserInputPort;
import com.sa.employee_service.users.application.outputports.CreateUserOutputPort;
import com.sa.employee_service.users.application.outputports.ExistsByUsernameOutputPort;
import com.sa.employee_service.users.application.services.CreateUserService;
import com.sa.employee_service.users.domain.User;
import com.sa.shared.exceptions.DuplicatedEntryException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserInputPort {
    private final CreateUserService createUserService;

    @Transactional(rollbackOn = Exception.class)
    public User handle(CreateUserDTO createUserRequestDTO) throws DuplicatedEntryException {
        return createUserService.createUser(createUserRequestDTO);
    }
}
