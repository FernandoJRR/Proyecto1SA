package com.sa.employee_service.users.application.inputports;

import com.sa.employee_service.users.domain.User;
import com.sa.shared.exceptions.NotFoundException;

public interface FindUserByUsernameInputPort {
    public User handle(String username) throws NotFoundException;
}
