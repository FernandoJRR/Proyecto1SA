package com.sa.employee_service.auth.infrastructure.restadapter.dtos;

import java.util.List;

import com.sa.employee_service.shared.infrastructure.dtos.EmployeeResponseDTO;
import com.sa.employee_service.shared.infrastructure.dtos.PermissionResponse;

import lombok.Value;

@Value
public class LoginResponse {

    String username;
    EmployeeResponseDTO employee;
    String token;
    List<PermissionResponse> permissions;
}
