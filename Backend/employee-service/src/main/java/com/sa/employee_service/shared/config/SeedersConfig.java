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


		List<Permission> permissionsHotelStaff = new ArrayList<>();
		List<Permission> permissionsRestaurantStaff = new ArrayList<>();
		List<Permission> permissionsAccountant = new ArrayList<>();

		// cremos los permisos
		for (SystemPermissionEnum permissionEnum : SystemPermissionEnum.values()) {
            Permission tempPermission = permissionEnum.getPermission();
            CreatePermissionDTO createPermissionDTO = new CreatePermissionDTO(
                tempPermission.getName(), tempPermission.getAction()
            );
			Permission createdPermission = createPermissionInputPort
					.handle(createPermissionDTO);

			createdPermissions.add(createdPermission);

            //Hotel Staff permissions
            if (permissionEnum.equals(SystemPermissionEnum.CREATE_RESERVATION)
            || permissionEnum.equals(SystemPermissionEnum.CANCEL_RESERVATION)
            || permissionEnum.equals(SystemPermissionEnum.PAY_RESERVATION)
            || permissionEnum.equals(SystemPermissionEnum.DELETE_RESERVATION)
            || permissionEnum.equals(SystemPermissionEnum.CREATE_PAYMENT)
            || permissionEnum.equals(SystemPermissionEnum.CREATE_CLIENT)
            ) {
                permissionsHotelStaff.add(createdPermission);
            }

            //Restaurant Staff permissions
            if (permissionEnum.equals(SystemPermissionEnum.CREATE_ORDER)
            || permissionEnum.equals(SystemPermissionEnum.CREATE_PAYMENT)
            || permissionEnum.equals(SystemPermissionEnum.CREATE_CLIENT)
            ) {
                permissionsRestaurantStaff.add(createdPermission);
            }

            //Accountant permissions
            if (permissionEnum.equals(SystemPermissionEnum.READ_REPORT)
            ) {
                permissionsAccountant.add(createdPermission);
            }
		}

        List<PermissionEntity> adminPermissionEntities = permissionRepositoryMapper.toEntityList(createdPermissions);
        List<PermissionEntity> hotelStaffPermissionEntities = permissionRepositoryMapper.toEntityList(permissionsHotelStaff);
        List<PermissionEntity> restaurantStaffPermissionEntities = permissionRepositoryMapper.toEntityList(permissionsRestaurantStaff);
        List<PermissionEntity> acountantPermissionEntities = permissionRepositoryMapper.toEntityList(permissionsAccountant);
		// mandamos a crear el tipo de empleado admin
        EmployeeType tempAdminType = EmployeeTypeEnum.ADMIN.getEmployeeType();
        tempAdminType.setId(UUID.randomUUID());
        tempAdminType.setPermissions(createdPermissions);
        EmployeeTypeEntity adminTypeEntity = new EmployeeTypeEntity(
            tempAdminType.getId().toString(),
            tempAdminType.getName()
        );
        adminTypeEntity.setPermissions(adminPermissionEntities);
		EmployeeTypeEntity adminEmployeeType = employeeTypeRepository.save(adminTypeEntity);

		// se crean los tipos de usuario
		EmployeeType hotelStaffType = EmployeeTypeEnum.STAFF_HOTEL.getEmployeeType();
        hotelStaffType.setId(UUID.randomUUID());
        hotelStaffType.setPermissions(permissionsHotelStaff);
        EmployeeTypeEntity hotelStaffTypeEntity = new EmployeeTypeEntity(
            hotelStaffType.getId().toString(),
            hotelStaffType.getName()
        );
        hotelStaffTypeEntity.setPermissions(hotelStaffPermissionEntities);
		employeeTypeRepository.save(hotelStaffTypeEntity);

		EmployeeType restaurantStaffType = EmployeeTypeEnum.STAFF_RESTAURANT.getEmployeeType();
        restaurantStaffType.setId(UUID.randomUUID());
        restaurantStaffType.setPermissions(permissionsRestaurantStaff);
        EmployeeTypeEntity restaurantStaffTypeEntity = new EmployeeTypeEntity(
            restaurantStaffType.getId().toString(),
            restaurantStaffType.getName()
        );
        restaurantStaffTypeEntity.setPermissions(restaurantStaffPermissionEntities);
		employeeTypeRepository.save(restaurantStaffTypeEntity);

		EmployeeType acountantType = EmployeeTypeEnum.CONTADOR.getEmployeeType();
        acountantType.setId(UUID.randomUUID());
        acountantType.setPermissions(permissionsAccountant);
        EmployeeTypeEntity acountantTypeEntity = new EmployeeTypeEntity(
            acountantType.getId().toString(),
            acountantType.getName()
        );
        acountantTypeEntity.setPermissions(acountantPermissionEntities);
		employeeTypeRepository.save(acountantTypeEntity);


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
