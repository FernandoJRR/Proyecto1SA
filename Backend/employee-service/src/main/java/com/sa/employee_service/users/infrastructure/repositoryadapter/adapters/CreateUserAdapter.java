package com.sa.employee_service.users.infrastructure.repositoryadapter.adapters;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.employee_service.users.application.outputports.CreateUserOutputPort;
import com.sa.employee_service.users.domain.User;
import com.sa.employee_service.users.infrastructure.repositoryadapter.mappers.UserRepositoryMapper;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateUserAdapter implements CreateUserOutputPort {
    private final UserRepository userRepository;
    private final UserRepositoryMapper userRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public User createUser(User user) {
        UserEntity entity = userRepositoryMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        return userRepositoryMapper.toDomain(saved);
    }
}
