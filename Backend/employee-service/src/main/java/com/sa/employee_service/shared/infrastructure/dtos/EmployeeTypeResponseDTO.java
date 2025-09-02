package com.sa.employee_service.shared.infrastructure.dtos;

import java.util.List;

import lombok.Value;

@Value
public class EmployeeTypeResponseDTO {

    String id;
    String name;
    List<PermissionResponseDTO> permissions;
}
