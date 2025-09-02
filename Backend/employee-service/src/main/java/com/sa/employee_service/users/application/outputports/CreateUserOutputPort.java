package com.sa.employee_service.users.application.outputports;

import com.sa.employee_service.users.domain.User;

public interface CreateUserOutputPort {
    public User createUser(User user);
}
