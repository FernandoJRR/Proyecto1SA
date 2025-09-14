package com.sa.employee_service.users.domain;

import java.time.LocalDate;
import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Auditor {

    private String username;
    private String password;
    private LocalDate desactivatedAt;

    public static User register(String username, String encodedPassword) {
        User u = new User();
        u.setId(UUID.randomUUID());
        u.username = username;
        u.password = encodedPassword;
        return u;
    }
}
