package com.sa.employee_service.auth.infrastructure.restadapter.mappers;

import org.mapstruct.Mapper;

import com.sa.employee_service.auth.application.dtos.LoginDTO;
import com.sa.employee_service.auth.infrastructure.restadapter.dtos.LoginResponse;

@Mapper(componentModel = "spring")
public interface LoginRestMapper {
    public LoginResponse toResponse(LoginDTO loginDTO);
}
