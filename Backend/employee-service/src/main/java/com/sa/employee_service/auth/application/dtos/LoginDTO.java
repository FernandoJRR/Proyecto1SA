package com.sa.employee_service.auth.application.dtos;

import java.util.List;

import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.domain.Permission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    public String username;
    public Employee employee;
    public String token;
    public List<Permission> permissions;
}
