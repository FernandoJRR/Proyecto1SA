package com.sa.employee_service.shared.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.application.dtos.CreatePermissionDTO;
import com.sa.employee_service.employees.application.inputports.CreateEmployeeInputPort;
import com.sa.employee_service.employees.application.inputports.CreatePermissionInputPort;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.employees.domain.Permission;
import com.sa.employee_service.employees.domain.enums.SystemPermissionEnum;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.mappers.PermissionRepositoryMapper;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.PermissionEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeTypeRepository;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.PermissionRepository;
import com.sa.employee_service.shared.domain.enums.EmployeeTypeEnum;
import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.shared.dtos.IdRequestDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Profile("dev || prod || local")
@RequiredArgsConstructor
@Component
public class SeedersConfig implements CommandLineRunner {

	private final CreateEmployeeInputPort createEmployeeInputPort;
    private final CreatePermissionInputPort createPermissionInputPort;
    private final PermissionRepositoryMapper permissionRepositoryMapper;

	private final PermissionRepository permissionRepository;
	//private final ParameterRepository parameterRepository;
	private final EmployeeTypeRepository employeeTypeRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void run(String... args) throws Exception {
		System.out.println("Ejecutnado el metodo de seeders.");
		if (permissionRepository.count() > 0) {
			return;
		}

		System.out.println("Creando los seeders.");
		// en este array guardaremos los pemrisos creados
		List<Permission> createdPermissions = new ArrayList<>();


		// cremos los permisos
		for (SystemPermissionEnum permissionEnum : SystemPermissionEnum.values()) {
            Permission tempPermission = permissionEnum.getPermission();
            CreatePermissionDTO createPermissionDTO = new CreatePermissionDTO(
                tempPermission.getName(), tempPermission.getAction()
            );
			Permission createdPermission = createPermissionInputPort
					.handle(createPermissionDTO);
			createdPermissions.add(createdPermission);
		}

        List<PermissionEntity> entitiesPermissions = permissionRepositoryMapper.toEntityList(createdPermissions);
		// mandamos a crear el tipo de empleado admin
        EmployeeType tempAdminType = EmployeeTypeEnum.ADMIN.getEmployeeType();
        tempAdminType.setId(UUID.randomUUID());
        tempAdminType.setPermissions(createdPermissions);
        EmployeeTypeEntity employeeTypeEntity = new EmployeeTypeEntity(
            tempAdminType.getId().toString(),
            tempAdminType.getName()
        );
        employeeTypeEntity.setPermissions(entitiesPermissions);

		EmployeeTypeEntity adminEmployeeType = employeeTypeRepository.save(employeeTypeEntity);

		// creamos el tipo de usuario
		EmployeeTypeEntity newEmployeeType = new EmployeeTypeEntity("USER");
        newEmployeeType.setId(UUID.randomUUID().toString());
		employeeTypeRepository.save(newEmployeeType);

        CreateUserDTO createUserDTO = new CreateUserDTO("admin", "admin");
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
        createEmployeeDTO.setCui("1234431123321");
        createEmployeeDTO.setFirstName("admin");
        createEmployeeDTO.setLastName("admin");
        createEmployeeDTO.setSalary(new BigDecimal(5000));
        createEmployeeDTO.setEmployeeTypeId(new IdRequestDTO(adminEmployeeType.getId()));
        createEmployeeDTO.setHiredAt(LocalDate.now());
        createEmployeeDTO.setCreateUserDTO(createUserDTO);
		createEmployeeInputPort.handle(createEmployeeDTO);
	}





}
