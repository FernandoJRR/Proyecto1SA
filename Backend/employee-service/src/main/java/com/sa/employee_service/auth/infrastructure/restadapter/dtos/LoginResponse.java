package com.sa.employee_service.auth.infrastructure.restadapter.dtos;

import java.util.List;

import com.sa.sharedEmployeeService.dto.EmployeeResponseDTO;
import com.sa.sharedEmployeeService.dto.PermissionResponse;

import lombok.Value;

@Value
public class LoginResponse {

    String username;
    EmployeeResponseDTO employee;
    String token;
    List<PermissionResponse> permissions;
}
