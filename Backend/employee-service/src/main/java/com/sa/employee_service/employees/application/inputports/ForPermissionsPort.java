package com.sa.employee_service.employees.application.inputports;

import java.util.List;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.shared.exceptions.*;

public interface ForPermissionsPort {

    public PermissionEntity createPermission(PermissionEntity permissionToCreate) throws DuplicatedEntryException;

    /**
     * Busca todos los permisos en base al id de los permisos enviados
     * (estos deben estar preferiblemente inicializados solo con el id).
     *
     * @param permissions permisos con solo el id inicializado
     * @return permisos encontrados con todos sus parametros inicializados
     * @throws NotFoundException
     */
    public List<PermissionEntity> findAllById(List<PermissionEntity> permissions) throws NotFoundException;

    public PermissionEntity findPermissionByName(PermissionEntity permission) throws NotFoundException;

    public PermissionEntity findPermissionById(PermissionEntity permission) throws NotFoundException;

    /**
     * Recupera todos los permisos registrados en el sistema.
     *
     * @return una lista de Permission con todos los permisos existentes.
     */
    public List<PermissionEntity> findAllPemrissions();

}
