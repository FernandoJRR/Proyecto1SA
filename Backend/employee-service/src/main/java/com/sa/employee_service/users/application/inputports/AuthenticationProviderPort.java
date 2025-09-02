package com.sa.employee_service.users.application.inputports;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.shared.exceptions.*;

public interface AuthenticationProviderPort {

    /**
     * Obtiene el empleado autenticado actualmente en el sistema.
     *
     * @return el objeto Employee correspondiente al usuario autenticado.
     * @throws IllegalStateException si no hay ningún usuario autenticado en el
     *                               contexto.
     * @throws NotFoundException     si no se encuentra un empleado asociado al
     *                               nombre de usuario autenticado.
     */
    public EmployeeEntity getAutenticatedEmployee() throws IllegalStateException, NotFoundException;
}
