package com.sa.employee_service.users.infrastructure.repositoryadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import com.sa.employee_service.users.domain.User;
import com.sa.employee_service.users.infrastructure.repositoryadapter.mappers.UserRepositoryMapper;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;
import com.sa.employee_service.users.infrastructure.repositoryadapter.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class CreateUserAdapterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRepositoryMapper userRepositoryMapper;

    @InjectMocks
    private CreateUserAdapter adapter;

    private User domainUser;
    private UserEntity entityToSave;
    private UserEntity savedEntity;
    private User domainFromSaved;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepare domain object using your domain User class (extends Auditor, etc.)
        UUID userId = UUID.randomUUID();
        domainUser = new User();
        domainUser.setId(userId);
        domainUser.setUsername("user_" + userId.toString());
        domainUser.setPassword("pwd123");
        // desactivatedAt is null by default

        // Prepare entity mapping
        entityToSave = new UserEntity(
            userId.toString(),
            domainUser.getUsername(),
            domainUser.getPassword()
        );
        // entityToSave.setEmployee(...) etc. if needed

        // After save: simulate saved entity (could be the same or modifications)
        savedEntity = new UserEntity(
            userId.toString(),
            domainUser.getUsername(),
            domainUser.getPassword()
        );
        // You may set desactivatedAt, employee, etc. if needed

        // Build domainFromSaved using domain class
        domainFromSaved = new User();
        domainFromSaved.setId(userId);
        domainFromSaved.setUsername(savedEntity.getUsername());
        domainFromSaved.setPassword(savedEntity.getPassword());
        // domainFromSaved.desactivatedAt null

        // Mock mapper/repository behavior
        when(userRepositoryMapper.toEntity(domainUser)).thenReturn(entityToSave);
        when(userRepository.save(entityToSave)).thenReturn(savedEntity);
        when(userRepositoryMapper.toDomain(savedEntity)).thenReturn(domainFromSaved);
    }

    @Test
    void createUser_savesAndReturnsDomainUser() {
        User result = adapter.createUser(domainUser);

        verify(userRepositoryMapper).toEntity(domainUser);
        verify(userRepository).save(entityToSave);
        verify(userRepositoryMapper).toDomain(savedEntity);

        assertNotNull(result);
        assertEquals(domainFromSaved.getId(), result.getId());
        assertEquals(domainFromSaved.getUsername(), result.getUsername());
        assertEquals(domainFromSaved.getPassword(), result.getPassword());
        assertNull(result.getDesactivatedAt(), "desactivatedAt should be null for new user");
    }

    // Optionally test behavior when mapper produces an entity with null or missing values, etc.
    // Also test that if repository throws exception, that bubbles up (if appropriate).
}