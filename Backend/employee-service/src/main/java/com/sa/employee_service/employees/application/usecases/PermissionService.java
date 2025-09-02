package com.sa.employee_service.employees.application.usecases;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sa.employee_service.employees.application.inputports.ForPermissionsPort;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.PermissionRepository;
import com.sa.shared.exceptions.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class PermissionService implements ForPermissionsPort {

    private final PermissionRepository permissionRepository;

    @Override
    public PermissionEntity createPermission(PermissionEntity permissionToCreate) throws DuplicatedEntryException {
        if (permissionRepository.existsByName(permissionToCreate.getName())) {
            throw new DuplicatedEntryException("Ya existe un permiso con el mismo nombre.");
        }

        return permissionRepository.save(permissionToCreate);
    }

    @Override
    public List<PermissionEntity> findAllById(List<PermissionEntity> permissions) throws NotFoundException {

        List<PermissionEntity> foundPermissions = new ArrayList<>();
        for (PermissionEntity permission : permissions) {
            // si se lanza excepcion entonces la propagamos
            PermissionEntity findedPermission = findPermissionById(permission);
            foundPermissions.add(findedPermission);
        }

        // si el for termina si nproblema entonces los ids de los permisos existen y se
        // pueden utilizar
        return foundPermissions;
    }

    @Override
    public PermissionEntity findPermissionByName(PermissionEntity permission) throws NotFoundException {
        String errorMessage = String.format(
                "El permiso %s no existe en el sistema.",
                permission.getName());
        return permissionRepository.findByName(permission.getName()).orElseThrow(
                () -> new NotFoundException(errorMessage));
    }

    @Override
    public PermissionEntity findPermissionById(PermissionEntity permission) throws NotFoundException {
        String errorMessage = String.format(
                "El permiso con id %s no existe en el sistema.",
                permission.getId());
        return permissionRepository.findById(permission.getId()).orElseThrow(
                () -> new NotFoundException(errorMessage));
    }

    @Override
    public List<PermissionEntity> findAllPemrissions() {
        return permissionRepository.findAll();
    }
}
