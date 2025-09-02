package com.sa.employee_service.users.infrastructure.repositoryadapter.adapters;

import org.springframework.stereotype.Component;

import com.sa.employee_service.users.application.outputports.ExistsByUsernameOutputPort;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsByUsernameAdapter implements ExistsByUsernameOutputPort {
    private final UserRepository userRepository;

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
