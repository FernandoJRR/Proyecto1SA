package com.sa.employee_service.employees.application.usecases;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sa.employee_service.employees.application.dtos.CreateEmployeeDTO;
import com.sa.employee_service.employees.application.dtos.FindEmployeesDTO;
import com.sa.employee_service.employees.application.outputports.CreateEmployeeOutputPort;
import com.sa.employee_service.employees.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.ExistsRestaurantByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeeTypeByIdOutputPort;
import com.sa.employee_service.employees.application.outputports.FindEmployeesOutputPort;
import com.sa.employee_service.employees.domain.Employee;
import com.sa.employee_service.employees.domain.EmployeeType;
import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.application.services.CreateUserService;
import com.sa.employee_service.users.domain.User;
import com.sa.shared.dtos.IdRequestDTO;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class CreateEmployeeUseCaseTest {

    @Mock private CreateEmployeeOutputPort createEmployeeOutputPort;
    @Mock private FindEmployeeTypeByIdOutputPort findEmployeeTypeByIdOutputPort;
    @Mock private FindEmployeesOutputPort findEmployeesOutputPort;
    @Mock private CreateUserService createUserService;
    @Mock private ExistsHotelByIdOutputPort existsHotelByIdOutputPort;
    @Mock private ExistsRestaurantByIdOutputPort existsRestaurantByIdOutputPort;

    private CreateEmployeeUseCase useCase;

    // Test data constants (follow EmployeesServiceTest style)
    private static final String EMPLOYEE_FIRST_NAME = "Michael";
    private static final String EMPLOYEE_LAST_NAME = "Kane";
    private static final BigDecimal EMPLOYEE_SALARY = new BigDecimal("1200");
    private static final String EMPLOYEE_CUI = "1234567890123"; // 13 digits as required
    private static final LocalDate EMPLOYEE_HIRED_AT = LocalDate.of(2022, 11, 23);

    private static final String USERNAME = "Luis";
    private static final String PASSWORD = "secret";

    private static final UUID EMPLOYEE_TYPE_UUID = UUID.fromString("967c5c2b-ded0-4f0f-a68a-9466ac9e32f4");
    private static final UUID HOTEL_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID RESTAURANT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174111");

    @BeforeEach
    void setup() {
        useCase = new CreateEmployeeUseCase(
            createEmployeeOutputPort,
            findEmployeeTypeByIdOutputPort,
            findEmployeesOutputPort,
            createUserService,
            existsHotelByIdOutputPort,
            existsRestaurantByIdOutputPort
        );
    }

    // given: valid input and no duplicate CUI
    // when: handle is invoked
    // then: creates user and employee via output port
    @Test
    void givenValidInput_whenHandle_thenCreatesEmployee() throws Exception {
        // given
        EmployeeType employeeType = new EmployeeType(EMPLOYEE_TYPE_UUID, "Waiter");
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(EMPLOYEE_TYPE_UUID))
            .thenReturn(Optional.of(employeeType));
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class)))
            .thenReturn(new ArrayList<>());
        when(createUserService.createUser(any(CreateUserDTO.class)))
            .thenReturn(User.register(USERNAME, PASSWORD));
        when(createEmployeeOutputPort.createEmployee(anyString(), any(Employee.class)))
            .thenAnswer(inv -> inv.getArgument(1)); // echo created employee

        CreateEmployeeDTO dto = baseDTO();

        // when
        Employee result = useCase.handle(dto);

        // then
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(EMPLOYEE_FIRST_NAME, result.getFirstName()),
            () -> assertEquals(EMPLOYEE_LAST_NAME, result.getLastName()),
            () -> assertEquals(EMPLOYEE_SALARY, result.getSalary())
        );
        verify(createEmployeeOutputPort, times(1)).createEmployee(anyString(), any(Employee.class));
    }

    // given: missing employee type in repository
    // when: handle is invoked
    // then: throws NotFoundException
    @Test
    void givenMissingEmployeeType_whenHandle_thenThrowsNotFound() throws Exception {
        // given
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(EMPLOYEE_TYPE_UUID))
            .thenReturn(Optional.empty());

        CreateEmployeeDTO dto = baseDTO();

        // when - then
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    // given: existing employee with same CUI
    // when: handle is invoked
    // then: throws DuplicatedEntryException
    @Test
    void givenExistingCui_whenHandle_thenThrowsDuplicatedEntry() throws Exception {
        // given
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(EMPLOYEE_TYPE_UUID))
            .thenReturn(Optional.of(new EmployeeType(EMPLOYEE_TYPE_UUID, "Waiter")));
        List<Employee> duplicates = new ArrayList<>();
        duplicates.add(new Employee());
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class)))
            .thenReturn(duplicates);

        CreateEmployeeDTO dto = baseDTO();

        // when - then
        assertThrows(DuplicatedEntryException.class, () -> useCase.handle(dto));
    }

    // given: establishmentId present but establishmentType is null
    // when: handle is invoked
    // then: throws InvalidParameterException
    @Test
    void givenEstablishmentIdWithoutType_whenHandle_thenThrowsInvalidParameter() throws Exception {
        // given
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(EMPLOYEE_TYPE_UUID))
            .thenReturn(Optional.of(new EmployeeType(EMPLOYEE_TYPE_UUID, "Waiter")));
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class)))
            .thenReturn(new ArrayList<>());

        CreateEmployeeDTO dto = baseDTO();
        dto.setEstablishmentId(HOTEL_ID);
        dto.setEstablishmentType(null);

        // when - then
        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    // given: HOTEL establishment type but hotel does not exist
    // when: handle is invoked
    // then: throws NotFoundException
    @Test
    void givenHotelTypeAndNotExists_whenHandle_thenThrowsNotFound() throws Exception {
        // given
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(EMPLOYEE_TYPE_UUID))
            .thenReturn(Optional.of(new EmployeeType(EMPLOYEE_TYPE_UUID, "Waiter")));
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class)))
            .thenReturn(new ArrayList<>());
        when(existsHotelByIdOutputPort.existsById(HOTEL_ID.toString())).thenReturn(false);

        CreateEmployeeDTO dto = baseDTO();
        dto.setEstablishmentId(HOTEL_ID);
        dto.setEstablishmentType("HOTEL");

        // when - then
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    // given: RESTAURANT establishment type but restaurant does not exist
    // when: handle is invoked
    // then: throws NotFoundException
    @Test
    void givenRestaurantTypeAndNotExists_whenHandle_thenThrowsNotFound() throws Exception {
        // given
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(EMPLOYEE_TYPE_UUID))
            .thenReturn(Optional.of(new EmployeeType(EMPLOYEE_TYPE_UUID, "Waiter")));
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class)))
            .thenReturn(new ArrayList<>());
        when(existsRestaurantByIdOutputPort.existsById(RESTAURANT_ID.toString())).thenReturn(false);

        CreateEmployeeDTO dto = baseDTO();
        dto.setEstablishmentId(RESTAURANT_ID);
        dto.setEstablishmentType("RESTAURANT");

        // when - then
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    // given: HOTEL establishment exists and no duplicates
    // when: handle is invoked
    // then: creates employee with establishment info
    @Test
    void givenHotelExists_whenHandle_thenCreatesEmployeeWithEstablishment() throws Exception {
        // given
        EmployeeType employeeType = new EmployeeType(EMPLOYEE_TYPE_UUID, "Waiter");
        when(findEmployeeTypeByIdOutputPort.findEmployeeTypeById(EMPLOYEE_TYPE_UUID))
            .thenReturn(Optional.of(employeeType));
        when(findEmployeesOutputPort.findEmployees(any(FindEmployeesDTO.class)))
            .thenReturn(new ArrayList<>());
        when(existsHotelByIdOutputPort.existsById(HOTEL_ID.toString())).thenReturn(true);
        when(createUserService.createUser(any(CreateUserDTO.class)))
            .thenReturn(User.register(USERNAME, PASSWORD));
        when(createEmployeeOutputPort.createEmployee(anyString(), any(Employee.class)))
            .thenAnswer(inv -> inv.getArgument(1));

        CreateEmployeeDTO dto = baseDTO();
        dto.setEstablishmentId(HOTEL_ID);
        dto.setEstablishmentType("HOTEL");

        // when
        Employee result = useCase.handle(dto);

        // then
        assertAll(
            () -> assertNotNull(result.getEstablishmentId()),
            () -> assertEquals(HOTEL_ID, result.getEstablishmentId()),
            () -> assertEquals("HOTEL", result.getEstablishmentType().name())
        );
        verify(createEmployeeOutputPort, times(1)).createEmployee(anyString(), any(Employee.class));
    }

    private static CreateEmployeeDTO baseDTO() {
        CreateEmployeeDTO dto = new CreateEmployeeDTO();
        dto.setFirstName(EMPLOYEE_FIRST_NAME);
        dto.setLastName(EMPLOYEE_LAST_NAME);
        dto.setSalary(EMPLOYEE_SALARY);
        dto.setCui(EMPLOYEE_CUI);
        dto.setEmployeeTypeId(new IdRequestDTO(EMPLOYEE_TYPE_UUID.toString()));
        dto.setHiredAt(EMPLOYEE_HIRED_AT);
        dto.setCreateUserDTO(new CreateUserDTO(USERNAME, PASSWORD));
        return dto;
    }
}
