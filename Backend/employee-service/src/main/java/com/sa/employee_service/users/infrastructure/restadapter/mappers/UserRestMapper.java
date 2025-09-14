package com.sa.employee_service.users.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.infrastructure.restadapter.dtos.CreateUserRequest;

@Mapper(
    componentModel = "spring")
public interface UserRestMapper  {
    public CreateUserDTO toDTO(CreateUserRequest createUserRequest);
}
