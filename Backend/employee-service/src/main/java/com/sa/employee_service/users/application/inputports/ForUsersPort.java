package com.sa.employee_service.users.application.inputports;

import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.shared.exceptions.*;

public interface ForUsersPort {

    public UserEntity findUserById(String userId) throws NotFoundException;

    public UserEntity findUserByUsername(String username) throws NotFoundException;
}
