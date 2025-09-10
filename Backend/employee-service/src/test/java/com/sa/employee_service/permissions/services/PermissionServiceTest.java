package com.sa.employee_service.permissions.services;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.PermissionRepository;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    private static final String PERMISSION_ID = "perm-001";
    private static final String PERMISSION_NAME = "GESTION_USUARIOS";

    private PermissionEntity permission;

    @BeforeEach
    void setUp() {
        permission = new PermissionEntity();
        permission.setId(PERMISSION_ID);
        permission.setName(PERMISSION_NAME);
    }

    /**
     * dado: que el nombre no está duplicado.
     * cuando: se llama a createPermission.
     * entonces: se guarda y retorna el permiso.
    @Test
    public void createPermissionShouldSucceedWhenNameNotExists() throws DuplicatedEntryException {
        when(permissionRepository.existsByName(PERMISSION_NAME)).thenReturn(false);
        when(permissionRepository.save(permission)).thenReturn(permission);

        PermissionEntity result = permissionService.createPermission(permission);

        assertNotNull(result);
        assertEquals(PERMISSION_NAME, result.getName());
        verify(permissionRepository).save(permission);
    }
     */

    /**
     * dado: que el nombre ya existe.
     * cuando: se llama a createPermission.
     * entonces: se lanza DuplicatedEntryException.
    @Test
    public void createPermissionShouldThrowWhenNameAlreadyExists() {
        when(permissionRepository.existsByName(PERMISSION_NAME)).thenReturn(true);

        assertThrows(DuplicatedEntryException.class, () -> permissionService.createPermission(permission));
        verify(permissionRepository, never()).save(any());
    }
     */

    /**
     * dado: que se pasa un permiso con ID válido.
     * cuando: se llama a findPermissionById.
     * entonces: se retorna el permiso.
    @Test
    public void findPermissionByIdShouldReturnWhenExists() throws NotFoundException {
        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.of(permission));

        PermissionEntity result = permissionService.findPermissionById(permission);

        assertNotNull(result);
        assertEquals(PERMISSION_ID, result.getId());
        verify(permissionRepository).findById(PERMISSION_ID);
    }
     */

    /**
     * dado: que el ID no existe.
     * cuando: se llama a findPermissionById.
     * entonces: se lanza NotFoundException.
    @Test
    public void findPermissionByIdShouldThrowWhenNotFound() {
        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> permissionService.findPermissionById(permission));
    }
     */

    /**
     * dado: que se pasa un permiso con nombre válido.
     * cuando: se llama a findPermissionByName.
     * entonces: se retorna el permiso.
    @Test
    public void findPermissionByNameShouldReturnWhenExists() throws NotFoundException {
        when(permissionRepository.findByName(PERMISSION_NAME)).thenReturn(Optional.of(permission));

        PermissionEntity result = permissionService.findPermissionByName(permission);

        assertNotNull(result);
        assertEquals(PERMISSION_NAME, result.getName());
        verify(permissionRepository).findByName(PERMISSION_NAME);
    }
     */

    /**
     * dado: que el nombre no existe.
     * cuando: se llama a findPermissionByName.
     * entonces: se lanza NotFoundException.
    @Test
    public void findPermissionByNameShouldThrowWhenNotFound() {
        when(permissionRepository.findByName(PERMISSION_NAME)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> permissionService.findPermissionByName(permission));
    }
     */

    /**
     * dado: que todos los permisos tienen ID válido.
     * cuando: se llama a findAllById.
     * entonces: se retorna la lista correspondiente.
    @Test
    public void findAllByIdShouldReturnAllWhenAllExist() throws NotFoundException {
        PermissionEntity another = new PermissionEntity();
        another.setId("perm-002");

        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.of(permission));
        when(permissionRepository.findById("perm-002")).thenReturn(Optional.of(another));

        List<PermissionEntity> input = List.of(permission, another);
        List<PermissionEntity> result = permissionService.findAllById(input);

        assertEquals(2, result.size());
    }
     */

    /**
     * dado: que uno de los permisos no existe.
     * cuando: se llama a findAllById.
     * entonces: se lanza NotFoundException.
    @Test
    public void findAllByIdShouldThrowWhenOneNotFound() {
        PermissionEntity unknown = new PermissionEntity();
        unknown.setId("missing-id");

        when(permissionRepository.findById("missing-id")).thenReturn(Optional.empty());

        List<PermissionEntity> input = List.of(unknown);
        assertThrows(NotFoundException.class, () -> permissionService.findAllById(input));
    }
     */

    /**
     * dado: que existen permisos en la base.
     * cuando: se llama a findAllPermissions.
     * entonces: se retorna la lista.
    @Test
    public void findAllPermissionsShouldReturnAll() {
        when(permissionRepository.findAll()).thenReturn(List.of(permission));

        List<PermissionEntity> result = permissionService.findAllPemrissions();

        assertEquals(1, result.size());
        verify(permissionRepository).findAll();
    }
     */
}
