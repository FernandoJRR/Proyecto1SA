package com.sa.employee_service.users.application.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.shared.utils.PasswordEncoderUtil;
import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.application.outputports.CreateUserOutputPort;
import com.sa.employee_service.users.application.outputports.ExistsByUsernameOutputPort;
import com.sa.employee_service.users.domain.User;
import com.sa.shared.exceptions.DuplicatedEntryException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUserService {
    private final CreateUserOutputPort createUserOutputPort;
    private final ExistsByUsernameOutputPort existsByUsernameOutputPort;
    private final PasswordEncoderUtil passwordEncoderUtil;

    @Transactional(propagation = Propagation.MANDATORY)
    public User createUser(CreateUserDTO createUserRequestDTO) throws DuplicatedEntryException {
        if (existsByUsernameOutputPort.existsByUsername(createUserRequestDTO.getUsername())) {
            throw new DuplicatedEntryException("Ya existe un usuario con el mismo nombre de usuario.");
        }

        String encoded = passwordEncoderUtil.encode(createUserRequestDTO.getPassword());

        User user = User.register(createUserRequestDTO.getUsername(), encoded);

        return createUserOutputPort.createUser(user);
    }
}
