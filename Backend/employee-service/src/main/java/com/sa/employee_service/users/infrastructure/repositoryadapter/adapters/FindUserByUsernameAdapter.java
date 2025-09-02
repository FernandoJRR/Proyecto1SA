package com.sa.employee_service.users.infrastructure.repositoryadapter.adapters;

import java.util.Optional;

import com.sa.employee_service.users.application.outputports.FindUserByUsernameOutputPort;
import com.sa.employee_service.users.domain.User;
import com.sa.employee_service.users.infrastructure.repositoryadapter.mappers.UserRepositoryMapper;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindUserByUsernameAdapter implements FindUserByUsernameOutputPort {
    private final UserRepository userRepository;
    private final UserRepositoryMapper userRepositoryMapper;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(userRepositoryMapper::toDomain);
    }

}
