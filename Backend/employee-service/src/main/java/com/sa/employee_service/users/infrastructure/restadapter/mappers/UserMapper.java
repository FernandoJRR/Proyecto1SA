package com.sa.employee_service.users.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public UserEntity fromCreateUserRequestDtoToUser(CreateUserDTO dto);
}
