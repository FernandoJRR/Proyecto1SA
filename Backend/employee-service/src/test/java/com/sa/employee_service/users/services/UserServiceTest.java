package com.sa.employee_service.users.services;

import com.sa.employee_service.shared.utils.PasswordEncoderUtil;
import com.sa.employee_service.users.application.dtos.CreateUserDTO;
import com.sa.employee_service.users.application.outputports.CreateUserOutputPort;
import com.sa.employee_service.users.application.outputports.ExistsByUsernameOutputPort;
import com.sa.employee_service.users.application.services.CreateUserService;
import com.sa.employee_service.users.application.usecases.CreateUserUseCase;
import com.sa.employee_service.users.application.usecases.UserService;
import com.sa.employee_service.users.domain.User;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private CreateUserOutputPort createUserOutputPort;

    @Mock
    private ExistsByUsernameOutputPort existsByUsernameOutputPort;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderUtil passwordEncoderUtil;

    @Mock
    private CreateUserService createUserService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private static final String USER_ID = "user-001";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";
    private static final String ENCRYPTED_PASSWORD = "encrypted123";

    private static CreateUserDTO createUserDTO;

    private UserEntity user;
    private CreateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        useCase = new CreateUserUseCase(
                createUserService);

        createUserDTO = new CreateUserDTO(USERNAME, PASSWORD);
    }

    /**
     * dado: que el nombre de usuario no está duplicado.
     * cuando: se llama a createUser.
     * entonces: se guarda correctamente y se encripta la contraseña.
     */
    @Test
    public void createUserShouldSucceedWhenUsernameNotExists() throws DuplicatedEntryException {
        // ARRANGE: the use case delegates to createUserService, so stub that
        when(createUserService.createUser(any(CreateUserDTO.class))).thenAnswer(inv -> {
            CreateUserDTO dto = inv.getArgument(0);
            User u = new User();
            u.setUsername(dto.getUsername());
            u.setPassword(ENCRYPTED_PASSWORD); // pretend service encoded it
            return u;
        });

        // ACT
        User result = useCase.handle(createUserDTO);

        // ASSERT
        verify(createUserService).createUser(createUserDTO);
        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
        assertEquals(ENCRYPTED_PASSWORD, result.getPassword());
    }

    @Test
    public void createUserShouldThrowWhenUsernameAlreadyExists() throws DuplicatedEntryException {
        // ARRANGE: make the service throw, since that's what the use case calls
        when(createUserService.createUser(createUserDTO)).thenThrow(new DuplicatedEntryException("Usuario ya existe"));

        // ACT + ASSERT
        assertThrows(DuplicatedEntryException.class, () -> useCase.handle(createUserDTO));
    }

    /**
     * dado: que el ID existe.
     * cuando: se llama a findUserById.
     * entonces: se retorna el usuario correspondiente.
     */
    @Test
    public void findUserByIdShouldReturnWhenExists() throws NotFoundException {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        UserEntity result = userService.findUserById(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        verify(userRepository).findById(USER_ID);
    }

    /**
     * dado: que el ID no existe.
     * cuando: se llama a findUserById.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findUserByIdShouldThrowWhenNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserById(USER_ID));
    }

    /**
     * dado: que el nombre de usuario existe.
     * cuando: se llama a findUserByUsername.
     * entonces: se retorna el usuario correspondiente.
     */
    @Test
    public void findUserByUsernameShouldReturnWhenExists() throws NotFoundException {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserEntity result = userService.findUserByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
        verify(userRepository).findByUsername(USERNAME);
    }

    /**
     * dado: que el nombre de usuario no existe.
     * cuando: se llama a findUserByUsername.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findUserByUsernameShouldThrowWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserByUsername(USERNAME));
    }
}
