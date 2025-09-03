package com.sa.employee_service.employees.application.usecases;
import com.sa.employee_service.employees.application.usecases.CreateEmployeeUseCase;
import com.sa.employee_service.employees.application.outputports.CreateEmployeeOutputPort;
import com.sa.employee_service.employees.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.ExistsRestaurantByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeTypeByIdOutputPort;
import com.sa.employee_service.users.application.inputports.CreateUserInputPort;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.application.inputports.ForEmployeeTypePort;
import com.sa.employee_service.employees.application.usecases.EmployeeService;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeHistory;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeTypeEntity;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.HistoryType;
import com.sa.employee_service.employees.infrastructure.repositoryadapter.repositories.EmployeeRepository;
import com.sa.employee_service.shared.domain.enums.EmployeeTypeEnum;
import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.application.inputports.ForUsersPort;
import com.sa.employee_service.users.application.services.CreateUserService;
import com.sa.employee_service.users.domain.User;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;
import com.sa.shared.dtos.IdRequestDTO;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.InvalidPeriodException;
import com.sa.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class EmployeesServiceTest {
        @Mock
        private EmployeeRepository employeeRepository;
        @Mock
        private UserRepository userRepository;

        @Mock
        private ForEmployeeTypePort forEmployeeTypePort;

        @Mock
        private ForUsersPort forUsersPort;

        @Mock
        private FindEmployeeTypeByIdOutputPort findEmployeeTypeByIdOutputPort;
        @Mock
        private CreateUserInputPort createUserInputPort;
        @Mock
        private CreateEmployeeOutputPort createEmployeeOutputPort;
        @Mock
        private CreateUserService createUserService;
        @Mock
        private ExistsHotelByIdOutputPort existsHotelByIdOutputPort;
        @Mock
        private ExistsRestaurantByIdOutputPort existsRestaurantByIdOutputPort;

        @InjectMocks
        private EmployeeService employeeService;

        private UserEntity user;
        private HistoryType historyType;
        private HistoryType historyTypeIncrease;
        private HistoryType historyTypeDecrease;
        private HistoryType historyTypeFiring;
        private EmployeeHistory employeeHistory;
        private EmployeeHistory reactivationHistory;
        private EmployeeEntity employee;
        private EmployeeEntity updatedEmployee;
        private EmployeeTypeEntity employeeType;
        private EmployeeTypeEntity employeeTypeDoctor;

        /** Para el nuevo empleado */
        private static final String CUI = "aafasdfaf231";
        private static final String EMPLOYEE_ID = "123e4567-e89b-42d3-a456-426655440000";
        private static final String EMPLOYEE_FIRST_NAME = "Michael";
        private static final String EMPLOYEE_LAST_NAME = "Kane";
        private static final BigDecimal EMPLOYEE_SALARY = new BigDecimal(1200);
        private static final BigDecimal EMPLOYEE_IGSS = new BigDecimal(10.2);
        private static final BigDecimal EMPLOYEE_IRTRA = new BigDecimal(10.2);

        /** Para el objeto usuario */
        private static final String USER_ID = "8db25021-8b82-4a60-a52c-d1d00324431";
        private static final String USER_NAME = "Luis";
        private static final String USER_PASSWORD = "123";

        /** Para el objeto tipo de empleado */
        private static final String EMPLOYEE_TYPE_ID = "967c5c2b-ded0-4f0f-a68a-9466ac9e32f4";
        private static final String EMPLOYEE_TYPE_ID_2 = "sdfg-sdfg-sdfg";

        /** Para actualizaciones */
        private static final String UPDATED_EMPLOYEE_CUI = "lkkdsdf";
        private static final String UPDATED_EMPLOYEE_FIRST_NAME = "Carlos";
        private static final String UPDATED_EMPLOYEE_LAST_NAME = "Ramírez";
        private static final BigDecimal UPDATED_EMPLOYEE_SALARY = new BigDecimal(7000);
        private static final BigDecimal UPDATED_EMPLOYEE_IGSS = new BigDecimal(5.25);
        private static final BigDecimal UPDATED_EMPLOYEE_IRTRA = new BigDecimal(10.2);

        /** Para el historial del empleado **/
        private static final String HISTORY_TYPE_ID = "fdsf-rtrer-bbvk";
        private static final String HISTORY_TYPE = "Contratacion";
        private static final String HISTORY_TYPE_ID_INCREASE = "rewp-fkds-bbvk";
        private static final String HISTORY_TYPE_INCREASE = "Aumento Salarial";
        private static final String HISTORY_TYPE_ID_DECREASE = "dflm-fodp-bbvk";
        private static final String HISTORY_TYPE_DECREASE = "Disminucion Salarial";
        private static final String HISTORY_TYPE_FIRING = "Despido";
        private static final String HISTORY_TYPE_ID_FIRING = "fdsj-ewoi-dsml";

        private static final String EMPLOYEE_HISTORY_ID = "rewf-fdsa-fdsd";
        private static final String EMPLOYEE_HISTORY_COMMENTARY = "Se realizo la contratacion con un salario de Q.7000";
        private static final LocalDate EMPLOYEE_HISTORY_LOCAL_DATE = LocalDate.of(2022, 11, 23);

        private static final String EMPLOYEE_REACTIVATION_HISTORY_ID = "klfd-dkns-fwsd";
        private static final String EMPLOYEE_REACTIVATION_HISTORY_COMMENTARY = "El empleado fue recontratado.";

        private static final LocalDate EMPLOYEE_REACTIVATION_LOCAL_DATE = LocalDate.of(2022, 3, 23);
        private static final LocalDate EMPLOYEE_DEACTIVATION_LOCAL_DATE = LocalDate.of(2022, 12, 23);
        private static final LocalDate EMPLOYEE_OLD_DEACTIVATION_LOCAL_DATE = LocalDate.of(2022, 1, 23);

        private static final BigDecimal EMPLOYEE_STARTING_SALARY = new BigDecimal(1200);
        private static final BigDecimal EMPLOYEE_NEW_SALARY = new BigDecimal(1500);
        private static final String EMPLOYEE_NEW_SALARY_COMMENTARY = "1500";
        private static final String EMPLOYEE_HISTORY_INCREASE_COMMENTARY = "Se realizo la contratacion con un salario de Q.1200";

        private static final BigDecimal EMPLOYEE_STARTING_DECREASE_SALARY = new BigDecimal(1500);
        private static final BigDecimal EMPLOYEE_NEW_SALARY_DECREASE = new BigDecimal(1200);
        private static final String EMPLOYEE_NEW_SALARY_DECREASE_COMMENTARY = "1200";
        private static final String EMPLOYEE_HISTORY_DECREASE_COMMENTARY = "Se realizo la contratacion con un salario de Q.1500";

        private static CreateEmployeeUseCase createEmployeeUseCase;

        private static User userDomain;

        /**
         * este metodo se ejecuta antes de cualquier prueba individual, se hace para
         * inicializar los moks ademas del driver
         */
        @BeforeEach
        private void setUp() {
                employee = new EmployeeEntity(CUI,
                                EMPLOYEE_FIRST_NAME,
                                EMPLOYEE_LAST_NAME,
                                EMPLOYEE_SALARY);
                employee.setId(EMPLOYEE_ID);

                updatedEmployee = new EmployeeEntity(UPDATED_EMPLOYEE_CUI,
                                UPDATED_EMPLOYEE_FIRST_NAME,
                                UPDATED_EMPLOYEE_LAST_NAME,
                                UPDATED_EMPLOYEE_SALARY);

                user = new UserEntity(USER_ID, USER_NAME, USER_PASSWORD);
                userDomain = User.register(USER_NAME, USER_PASSWORD);

                historyType = new HistoryType(HISTORY_TYPE);
                historyType.setId(HISTORY_TYPE_ID);

                historyTypeIncrease = new HistoryType(HISTORY_TYPE_INCREASE);
                historyTypeIncrease.setId(HISTORY_TYPE_ID_INCREASE);

                historyTypeDecrease = new HistoryType(HISTORY_TYPE_DECREASE);
                historyTypeDecrease.setId(HISTORY_TYPE_ID_DECREASE);

                historyTypeFiring = new HistoryType(HISTORY_TYPE_FIRING);
                historyTypeFiring.setId(HISTORY_TYPE_ID_FIRING);

                employeeHistory = new EmployeeHistory(EMPLOYEE_HISTORY_COMMENTARY);
                employeeHistory.setHistoryDate(EMPLOYEE_HISTORY_LOCAL_DATE);
                employeeHistory.setId(EMPLOYEE_HISTORY_ID);

                reactivationHistory = new EmployeeHistory(EMPLOYEE_REACTIVATION_HISTORY_COMMENTARY);
                reactivationHistory.setHistoryDate(EMPLOYEE_REACTIVATION_LOCAL_DATE);
                reactivationHistory.setId(EMPLOYEE_REACTIVATION_HISTORY_ID);

                employeeType = new EmployeeTypeEntity();
                employeeType.setId(EMPLOYEE_TYPE_ID);

                // inicializamos los empleados que usaremos para la reasignacion del tipo de
                // empleado
                employeeToReasignEmployeeType1 = new EmployeeEntity(EMPLOYEE_ID_1);
                employeeToReasignEmployeeType2 = new EmployeeEntity(EMPLOYEE_ID_2);


                createEmployeeUseCase = new CreateEmployeeUseCase(
                    createEmployeeOutputPort,
                    findEmployeeTypeByIdOutputPort,
                    createUserService,
                    existsHotelByIdOutputPort,
                    existsRestaurantByIdOutputPort
                );
        }

        @Test
        public void shouldInsertEmployee() throws DuplicatedEntryException, NotFoundException, InvalidParameterException {
            // Arrange
            UUID typeId = UUID.fromString(employeeType.getId());

            when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(any(UUID.class)))
                .thenReturn(Optional.of(mock(EmployeeType.class)));

            when(createUserService.createUser(any(CreateUserDTO.class)))
                .thenReturn(User.register(USER_NAME, USER_PASSWORD));

            when(createEmployeeOutputPort.createEmployee(anyString(), any(Employee.class)))
                .thenAnswer(inv -> inv.getArgument(1)); // return Employee passed in

            // Build input DTO expected by the use case
            CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
            createEmployeeDTO.setCui(employee.getCui());
            createEmployeeDTO.setFirstName(employee.getFirstName());
            createEmployeeDTO.setLastName(employee.getLastName());
            createEmployeeDTO.setSalary(employee.getSalary());
            createEmployeeDTO.setEmployeeTypeId(new IdRequestDTO(employeeType.getId()));
            createEmployeeDTO.setHiredAt(EMPLOYEE_HISTORY_LOCAL_DATE); // reuse constant date from the test
            createEmployeeDTO.setCreateUserDTO(new CreateUserDTO(USER_NAME, USER_PASSWORD));

            // System under test: the use case
            CreateEmployeeUseCase useCase = new CreateEmployeeUseCase(
                createEmployeeOutputPort,
                findEmployeeTypeByIdOutputPort,
                createUserService,
                existsHotelByIdOutputPort,
                existsRestaurantByIdOutputPort
            );

            // Act
            Employee result = useCase.handle(createEmployeeDTO);

            // Assert
            assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(EMPLOYEE_FIRST_NAME, result.getFirstName()),
                () -> assertEquals(EMPLOYEE_LAST_NAME, result.getLastName()),
                () -> assertEquals(EMPLOYEE_SALARY, result.getSalary())
            );

            // Verify the adapter interaction instead of repository save
            verify(createEmployeeOutputPort, times(1)).createEmployee(any(String.class), any(Employee.class));
        }

        /*
        @Test
        public void shouldNotInsertEmployeeWithExistantUsername() throws DuplicatedEntryException, NotFoundException {

                // ARRANGE
                CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO(user.getUsername(), user.getPassword());
                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenReturn(employeeType);

                CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
                createEmployeeDTO.setCui(employee.getCui());
                createEmployeeDTO.setFirstName(employee.getFirstName());
                createEmployeeDTO.setLastName(employee.getLastName());
                createEmployeeDTO.setSalary(employee.getSalary());
                IdRequestDTO idRequestDTO = new IdRequestDTO(employee.getId());
                createEmployeeDTO.setEmployeeTypeId(idRequestDTO);

                // ACT and Asserts
                assertThrows(DuplicatedEntryException.class, () -> {
                        // se verifica que se haya lanzado la excepcion
                        employeeService.createEmployee(createEmployeeDTO);
                });

                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
                verify(employeeRepository, times(0)).save(employee);

        }
         */

        @Test
        public void shouldNotInsertEmployeeWithInexistantEmployeeType()
                        throws NotFoundException, DuplicatedEntryException {
            // ARRANGE
            when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(UUID.fromString(employee.getId())))
                .thenReturn(Optional.empty());


            CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
            createEmployeeDTO.setCui(employee.getCui());
            createEmployeeDTO.setFirstName(employee.getFirstName());
            createEmployeeDTO.setLastName(employee.getLastName());
            createEmployeeDTO.setSalary(employee.getSalary());
            createEmployeeDTO.setEmployeeTypeId(new IdRequestDTO(employee.getId())); // mismatched id to trigger not found
            createEmployeeDTO.setHiredAt(EMPLOYEE_HISTORY_LOCAL_DATE);
            createEmployeeDTO.setCreateUserDTO(new CreateUserDTO(USER_NAME, USER_PASSWORD));

            // Act & Assert
            assertThrows(NotFoundException.class, () -> createEmployeeUseCase.handle(createEmployeeDTO));
        }

        @Test
        public void updateEmployeeShouldUpdateFieldsAndSave() throws NotFoundException {

                // ARRANGE
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenReturn(employeeType);
                when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employee);

                // ACT
                EmployeeEntity updateEmployee = employeeService.updateEmployee(employee.getId(), updatedEmployee,
                                employeeType);

                // validar que los setters fueron llamados con los valores correctos
                assertAll(
                                () -> assertEquals(UPDATED_EMPLOYEE_FIRST_NAME, updateEmployee.getFirstName()),
                                () -> assertEquals(UPDATED_EMPLOYEE_FIRST_NAME, updateEmployee.getFirstName()),
                                () -> assertEquals(UPDATED_EMPLOYEE_LAST_NAME, updateEmployee.getLastName()),
                                () -> assertEquals(UPDATED_EMPLOYEE_SALARY, updateEmployee.getSalary()),
                                () -> assertEquals(employeeType, updateEmployee.getEmployeeType()));

                // Asegurar que se guardó en la base de datos
                verify(employeeRepository, times(1)).findById(anyString());
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
                verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));

        }

        @Test
        public void updateEmployeeShouldNotUpdateEmployeeWithInexistantEmployee() throws NotFoundException {
                // ARRANGE
                // cuando se busque el empleado por id entonces volvr vacio para que lance la
                // excepcion
                when(employeeRepository.findById(anyString())).thenReturn(Optional.empty());

                // ACT
                assertThrows(NotFoundException.class, () -> {
                        employeeService.updateEmployee(anyString(), employee, employeeType);
                });

                // Asserts
                verify(employeeRepository, times(1)).findById(anyString());
                verify(forEmployeeTypePort, times(0)).existsEmployeeTypeById(anyString());
                verify(employeeRepository, times(0)).save(employee);

        }

        @Test
        public void updateEmployeeShouldNotUpdateEmployeeWithInexistantEmployeeType() throws NotFoundException {
                // ARRANGE
                // cuando se busque por id entonces devolver el employee
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));

                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenThrow(
                                NotFoundException.class);
                // ACT
                assertThrows(NotFoundException.class, () -> {
                        employeeService.updateEmployee(anyString(), employee, employeeType);
                });

                // ASSERTS
                verify(employeeRepository, times(1)).findById(anyString());
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
                verify(employeeRepository, times(0)).save(employee);

        }

        @Test
        public void shouldReactivateEmployeeSuccessfully()
                        throws NotFoundException, IllegalStateException, InvalidPeriodException {
                // ARRANGE
                employee.setUser(user);
                employee.setDesactivatedAt(EMPLOYEE_OLD_DEACTIVATION_LOCAL_DATE);
                user.setDesactivatedAt(EMPLOYEE_OLD_DEACTIVATION_LOCAL_DATE);

                when(employeeRepository.findById(eq(EMPLOYEE_ID))).thenReturn(Optional.of(employee));

                when(employeeRepository.save(any(EmployeeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // ACT
                EmployeeEntity reactivatedEmployee = employeeService.reactivateEmployee(EMPLOYEE_ID,
                                EMPLOYEE_REACTIVATION_LOCAL_DATE);

                // ASSERT
                ArgumentCaptor<EmployeeEntity> employeeCaptor = ArgumentCaptor.forClass(EmployeeEntity.class);
                verify(employeeRepository).save(employeeCaptor.capture());
                EmployeeEntity capturedEmployee = employeeCaptor.getValue();

                assertAll(
                                () -> assertNotNull(reactivatedEmployee, "Reactivated employee should not be null"),
                                () -> assertEquals(null, capturedEmployee.getDesactivatedAt(),
                                                "Employee deactivation date should be null after reactivation"),
                                () -> assertEquals(null, capturedEmployee.getUser().getDesactivatedAt(),
                                                "User deactivation date should be null after reactivation"));
        }

        @Test
        public void testDesactivateEmployee() throws NotFoundException, IllegalStateException, InvalidPeriodException {
                // ARRANGE
                HistoryType reason = new HistoryType("Motivo de Desactivación");
                reason.setId("reason-id-123");

                employee.setUser(user);

                when(employeeRepository.findById(eq(EMPLOYEE_ID))).thenReturn(Optional.of(employee));

                EmployeeHistory deactivationHistory = new EmployeeHistory("Empleado desactivado.");
                deactivationHistory.setHistoryDate(EMPLOYEE_DEACTIVATION_LOCAL_DATE);
                when(employeeRepository.save(any(EmployeeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // ACT
                EmployeeEntity deactivatedEmployee = employeeService.desactivateEmployee(EMPLOYEE_ID,
                                EMPLOYEE_DEACTIVATION_LOCAL_DATE, reason);

                // ASSERT
                ArgumentCaptor<EmployeeEntity> employeeCaptor = ArgumentCaptor.forClass(EmployeeEntity.class);
                verify(employeeRepository).save(employeeCaptor.capture());
                EmployeeEntity capturedEmployee = employeeCaptor.getValue();

                assertAll(
                                () -> assertNotNull(deactivatedEmployee, "The returned employee should not be null"),
                                () -> assertEquals(EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                                capturedEmployee.getDesactivatedAt(),
                                                "Employee deactivation date should match"),
                                () -> assertEquals(EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                                capturedEmployee.getUser().getDesactivatedAt(),
                                                "User deactivation date should match"));
        }

        /**
         * dado: que el empleado ya está desactivado en el sistema.
         * cuando: se intenta desactivar nuevamente.
         * entonces: se lanza una excepción IllegalStateException y no se realizan
         * cambios.
         */
        @Test
        public void desactivateEmployeeShouldThrowIllegalStateExceptionWhenEmployeeIsAlreadyDeactivated()
                        throws NotFoundException {
                // ARRANGE
                employee.setUser(user); // aseguramos que el empleado tenga un usuario
                // hacemos que el usuario ya este desactivado
                employee.setDesactivatedAt(LocalDate.now());
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));

                // ACT y ASSERT
                assertThrows(IllegalStateException.class,
                                () -> {
                                        employeeService.desactivateEmployee(EMPLOYEE_ID,
                                                        EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                                        any(HistoryType.class));
                                });
        }

        @Test
        public void desactivateEmployeeShouldNotDesactivateEmployeeWithInexistantEmployee() throws NotFoundException {
                // ARRANGE
                // cuando se busque por id mandamos vacio para que se lance la excepcion
                when(employeeRepository.findById(anyString())).thenReturn(Optional.empty());

                // ACT
                // Asserts
                assertThrows(NotFoundException.class, () -> {
                        employeeService.desactivateEmployee(anyString(), EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                        historyTypeFiring);
                });
        }

        /**
         * dado: que el empleado y el tipo de empleado existen en la base de datos.
         * cuando: se reasigna el tipo de empleado a un nuevo tipo válido.
         * entonces: el empleado debe tener actualizado el nuevo tipo de empleado.
         */
        @Test
        public void reassignEmployeeTypeShouldReassignEmployeeTypeSuccessfully() throws NotFoundException {

                // ARRANGE
                // cuando se busque por el id entonoces devolver nuestro mock de empleado
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
                // cuando se buswque e tipo de empleado a asignar entonces devolver nuestro mock
                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenReturn(employeeType);

                // ACT
                EmployeeEntity result = employeeService.reassignEmployeeType(EMPLOYEE_ID, EMPLOYEE_TYPE_ID);

                // ASSERT
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(employeeType, result.getEmployeeType()));

                verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);
        }

        /**
         * dado: que el empleado no existe en la base de datos.
         * cuando: se intenta reasignar su tipo de empleado.
         * entonces: se lanza una excepción `NotFoundException` y no se realizan
         * cambios.
         *
         * @throws NotFoundException
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenEmployeeDoesNotExist() throws NotFoundException {
                // ARRANGE
                // al devolver el empty en el findBi entonces el metodo debe lanzar un not found
                when(employeeRepository.findById(anyString())).thenReturn(Optional.empty());

                // ACT &y ASSERT
                assertThrows(NotFoundException.class,
                                () -> employeeService.reassignEmployeeType(EMPLOYEE_ID, EMPLOYEE_TYPE_ID));

                verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
                verify(forEmployeeTypePort, never()).findEmployeeTypeById(anyString());
        }

        /**
         * dado: que el tipo de empleado no existe en la base de datos.
         * cuando: se intenta reasignar el empleado a ese tipo de empleado inexistente.
         * entonces: se lanza una excepción `NotFoundException` y no se realizan
         * cambios.
         *
         * @throws NotFoundException
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenEmployeeTypeDoesNotExist() throws NotFoundException {
                // ARRANGE
                // si deolvemos el optional lleno
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
                // cuando mandemos a busqcar el tpio de empleado a asignar simulamos que este no
                // se encontro
                when(forEmployeeTypePort.findEmployeeTypeById(anyString()))
                                .thenThrow(new NotFoundException(anyString()));

                // ACT y ASSERT
                assertThrows(NotFoundException.class,
                                () -> employeeService.reassignEmployeeType(EMPLOYEE_ID, EMPLOYEE_TYPE_ID));

                verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);

        }

        private static final String EMPLOYEE_ID_1 = "sdaf-asdf-sad";
        private static final String EMPLOYEE_ID_2 = "";

        EmployeeEntity employeeToReasignEmployeeType1;
        EmployeeEntity employeeToReasignEmployeeType2;

        /**
         * dado: que una lista de empleados y un tipo de empleado existen en la base de
         * datos.
         * cuando: se reasignan todos los empleados a un nuevo tipo de empleado válido.
         * entonces: todos los empleados deben tener actualizado el nuevo tipo de
         * empleado.
         */
        @Test
        public void shouldReassignEmployeeTypeForMultipleEmployeesSuccessfully() throws NotFoundException {
                // ARRANGE
                List<EmployeeEntity> employees = List.of(employeeToReasignEmployeeType1, employeeToReasignEmployeeType2);

                when(employeeRepository.findById(EMPLOYEE_ID_1))
                                .thenReturn(Optional.of(employeeToReasignEmployeeType1));
                when(employeeRepository.findById(EMPLOYEE_ID_2))
                                .thenReturn(Optional.of(employeeToReasignEmployeeType2));
                when(forEmployeeTypePort.findEmployeeTypeById(EMPLOYEE_TYPE_ID)).thenReturn(employeeType);

                // ACT
                List<EmployeeEntity> result = employeeService.reassignEmployeeType(employees, EMPLOYEE_TYPE_ID);

                // ASSERT
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(2, result.size()),
                                () -> assertEquals(employeeType, result.get(0).getEmployeeType()),
                                () -> assertEquals(employeeType, result.get(1).getEmployeeType()));

                verify(employeeRepository, times(2)).findById(anyString());
                verify(forEmployeeTypePort, times(2)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);
        }

        /**
         * dado: que al menos un empleado de la lista no existe en la base de datos.
         * cuando: se intenta reasignar su tipo de empleado.
         * entonces: se lanza una excepción `NotFoundException` y no se realizan cambios
         * en ningún empleado.
         *
         * @throws NotFoundException
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenAnyEmployeeDoesNotExist() throws NotFoundException {
                // ARRANGE
                List<EmployeeEntity> employees = List.of(employeeToReasignEmployeeType1, employeeToReasignEmployeeType2);

                when(employeeRepository.findById(EMPLOYEE_ID_1))
                                .thenReturn(Optional.of(employeeToReasignEmployeeType1));
                // cuando busquemos el segundo id entonces devolvemos un Optional vacio para
                // forzar el NotFound
                when(employeeRepository.findById(EMPLOYEE_ID_2)).thenReturn(Optional.empty());

                // ACT yASSERT
                assertThrows(NotFoundException.class,
                                () -> employeeService.reassignEmployeeType(employees, EMPLOYEE_TYPE_ID));

                verify(employeeRepository, times(2)).findById(anyString());
                // solo se debra hacer realizado una busqueda del tipo de empledo porque a la
                // segunda vez ya habra fallado antes de llegar alli
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
        }

        /**
         * dado: que el tipo de empleado no existe en la base de datos.
         * cuando: se intenta reasignar una lista de empleados a ese tipo de empleado
         * inexistente.
         * entonces: se lanza una excepción `NotFoundException` y no se realizan cambios
         * en ningún empleado.
         *
         * @throws NotFoundException
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenEmployeeTypeDoesNotExistForMultipleEmployees()
                        throws NotFoundException {
                // ARRANGE
                List<EmployeeEntity> employees = List.of(employeeToReasignEmployeeType1, employeeToReasignEmployeeType2);

                when(employeeRepository.findById(EMPLOYEE_ID_1))
                                .thenReturn(Optional.of(employeeToReasignEmployeeType1));

                when(forEmployeeTypePort.findEmployeeTypeById(EMPLOYEE_TYPE_ID))
                                .thenThrow(new NotFoundException(anyString()));

                // ACT y ASSERT
                assertThrows(NotFoundException.class,
                                () -> employeeService.reassignEmployeeType(employees, EMPLOYEE_TYPE_ID));

                // solo se debera realzar una vez porque fallara el buscar el tipo de empleado
                verify(employeeRepository, times(1)).findById(anyString());
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);
        }

        /**
         * dado: que existen empleados en la base de datos.
         * cuando: se consulta la lista de empleados.
         * entonces: el método devuelve una lista con los empleados existentes.
         */
        @Test
        public void shouldReturnListOfEmployeesWhenEmployeesExist() {
                // ARRANGE
                List<EmployeeEntity> employees = List.of(employee, updatedEmployee);
                when(employeeRepository.findAll()).thenReturn(employees);

                // ACT
                List<EmployeeEntity> result = employeeService.findEmployees();

                // ASSERT
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(2, result.size()));

                verify(employeeRepository, times(1)).findAll();
        }

        /**
         * dado: que existe un tipo de empleado válido en la base de datos.
         * cuando: se busca a los empleados por ese tipo y se proporciona un término de
         * búsqueda.
         * entonces: se devuelve una lista con los empleados que coinciden con el nombre
         * o apellido buscado.
         */
        @Test
        public void shouldReturnEmployeesByTypeWithMatchingSearch() throws NotFoundException {
                // ARRANGE
                String search = "Luis";
                List<EmployeeEntity> expectedEmployees = List.of(employee);

                when(forEmployeeTypePort.findEmployeeTypeById(EMPLOYEE_TYPE_ID)).thenReturn(employeeType);
                when(employeeRepository.findAll(ArgumentMatchers.<Specification<EmployeeEntity>>any()))
                                .thenReturn(expectedEmployees);

                // ACT
                List<EmployeeEntity> result = employeeService.getEmployeesByType(EMPLOYEE_TYPE_ID, search);

                // ASSERT
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(1, result.size()),
                                () -> assertEquals(employee.getFirstName(), result.get(0).getFirstName()));

                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);
                verify(employeeRepository, times(1)).findAll(ArgumentMatchers.<Specification<EmployeeEntity>>any());
        }

        /**
         * dado: que el tipo de empleado no existe en la base de datos.
         * cuando: se intenta obtener empleados por ese tipo.
         * entonces: se lanza una excepción `NotFoundException` y no se realiza ninguna
         * consulta a la base de datos.
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenTypeNotFoundInGetEmployeesByType() throws NotFoundException {
                // ARRANGE
                when(forEmployeeTypePort.findEmployeeTypeById(anyString()))
                                .thenThrow(new NotFoundException("Tipo de empleado no encontrado"));

                // ACT & ASSERT
                assertThrows(NotFoundException.class, () -> {
                        employeeService.getEmployeesByType("invalid-id", "Luis");
                });

                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById("invalid-id");
                verify(employeeRepository, never()).findAll(ArgumentMatchers.<Specification<EmployeeEntity>>any());
        }

        /**
         * dado: que existe un empleado registrado con el nombre de usuario
         * proporcionado.
         * cuando: se busca el empleado por su nombre de usuario.
         * entonces: se retorna el empleado correspondiente sin lanzar ninguna
         * excepción.
         */
        @Test
        public void shouldReturnEmployeeWhenEmployeeExistsByUsername() throws NotFoundException {
                // arrange
                when(employeeRepository.findByUser_Username(anyString())).thenReturn(Optional.of(employee));

                // act
                EmployeeEntity result = employeeService.findEmployeeByUsername(USER_NAME);

                // assert
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(EMPLOYEE_ID, result.getId()),
                                () -> assertEquals(EMPLOYEE_FIRST_NAME, result.getFirstName()));
        }

        /**
         * dado: que no existe un empleado registrado con el nombre de usuario
         * proporcionado.
         * cuando: se intenta buscar el empleado por su nombre de usuario.
         * entonces: se lanza una excepción NotFoundException indicando que no fue
         * encontrado.
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenEmployeeNotFoundByUsername() {
                // arrange
                when(employeeRepository.findByUser_Username(anyString())).thenReturn(Optional.empty());

                // act y assert
                assertThrows(
                                NotFoundException.class,
                                () -> employeeService.findEmployeeByUsername(USER_NAME));

        }

}
