package com.sa.employee_service.employees.application.inputports;

import java.util.List;

import com.sa.shared.exceptions.NotFoundException;

public interface FindPermissionsActionsByUsernameInputPort {
    public List<String> handle(String username) throws NotFoundException;
}
