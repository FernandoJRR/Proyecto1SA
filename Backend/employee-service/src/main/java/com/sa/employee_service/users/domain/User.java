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
        u.username = requireNonBlank(username, "username");
        u.password = requireNonBlank(encodedPassword, "encodedPassword");
        return u;
    }

    /** Change password with an already-encoded value. */
    public void changePasswordEncoded(String newEncodedPassword) {
        this.password = requireNonBlank(newEncodedPassword, "newEncodedPassword");
    }

    /** Deactivate the user at a given date. */
    public void deactivate(LocalDate when) {
        if (this.desactivatedAt != null) {
            throw new IllegalStateException("User already deactivated");
        }
        this.desactivatedAt = when != null ? when : LocalDate.now();
    }

    /** Reactivate the user (clears deactivation flag). */
    public void reactivate() {
        if (this.desactivatedAt == null) {
            throw new IllegalStateException("User already active");
        }
        this.desactivatedAt = null;
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
