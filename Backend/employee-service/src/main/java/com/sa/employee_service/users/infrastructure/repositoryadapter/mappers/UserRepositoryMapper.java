package com.sa.employee_service.users.infrastructure.repositoryadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.employee_service.users.domain.User;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;

@Mapper(componentModel = "spring")
public interface UserRepositoryMapper {
    public UserEntity toEntity(User user);
    public User toDomain(UserEntity user);
}
