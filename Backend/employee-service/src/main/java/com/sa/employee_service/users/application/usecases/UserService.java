package com.sa.employee_service.users.application.usecases;

import org.springframework.stereotype.Service;

import com.sa.employee_service.shared.utils.PasswordEncoderUtil;
import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.application.inputports.ForUsersPort;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;
import com.sa.shared.exceptions.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements ForUsersPort {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public UserEntity findUserById(String userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "El id especificado no pertenece a ningun usuario en el sistema"));
    }

    public UserEntity findUserByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(
                "El nombre de usuario especificado no pertenece a ningun usuario en el sistema"));
    }

}
