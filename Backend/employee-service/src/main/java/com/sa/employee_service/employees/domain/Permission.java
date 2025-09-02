package com.sa.employee_service.employees.domain;

import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission extends Auditor {

    private String name;
    private String action;

    public Permission(UUID id, String name, String action) {
        super(id);
        this.name = name;
        this.action = action;
    }

    public static Permission create(String name, String action) {
        return new Permission(UUID.randomUUID(), name, action);
    }
}
