package com.sa.employee_service.users.application.outputports;

import java.util.Optional;

import com.sa.employee_service.users.domain.User;

public interface FindUserByUsernameOutputPort {
    public Optional<User> findByUsername(String username);
}
