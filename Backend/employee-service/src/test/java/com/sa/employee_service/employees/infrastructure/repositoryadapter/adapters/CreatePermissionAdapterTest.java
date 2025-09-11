package com.sa.employee_service.employees.infrastructure.repositoryadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.domain.Permission;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.PermissionRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.PermissionRepository;

@ExtendWith(MockitoExtension.class)
public class CreatePermissionAdapterTest {

    @Mock private PermissionRepository permissionRepository;
    @Mock private PermissionRepositoryMapper permissionRepositoryMapper;

    private CreatePermissionAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new CreatePermissionAdapter(permissionRepository, permissionRepositoryMapper);
    }

    // given: permission to create
    // when: createPermission is called
    // then: maps to entity, saves, maps back to domain
    @Test
    void givenPermission_whenCreate_thenSavesAndMapsBack() {
        Permission domain = Permission.create("Users", "USERS_READ");
        PermissionEntity entity = new PermissionEntity("Users", "USERS_READ");

        when(permissionRepositoryMapper.toEntity(same(domain))).thenReturn(entity);
        when(permissionRepository.save(same(entity))).thenReturn(entity);
        when(permissionRepositoryMapper.toDomain(same(entity))).thenReturn(domain);

        Permission result = adapter.createPermission(domain);

        assertSame(domain, result);
        verify(permissionRepositoryMapper, times(1)).toEntity(same(domain));
        verify(permissionRepository, times(1)).save(same(entity));
        verify(permissionRepositoryMapper, times(1)).toDomain(same(entity));
    }
}

