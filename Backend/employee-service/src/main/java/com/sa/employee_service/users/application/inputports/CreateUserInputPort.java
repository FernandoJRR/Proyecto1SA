package com.sa.employee_service.users.application.inputports;

import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.domain.User;
import com.sa.shared.exceptions.DuplicatedEntryException;

public interface CreateUserInputPort {
    public User handle(CreateUserDTO createUserDTO) throws DuplicatedEntryException;
}
