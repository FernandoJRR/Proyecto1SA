package com.sa.employee_service.auth.application.outputports;

import java.util.List;

import com.sa.employee_service.users.domain.User;

public interface GenerateJWTTokenOutputPort {
    /**
     * Genera un token jwt con los permisos del usuario
     *
     * @param user
     * @param permissions
     * @return
     */
    public String generateToken(User user, List<String> permissions);
}
