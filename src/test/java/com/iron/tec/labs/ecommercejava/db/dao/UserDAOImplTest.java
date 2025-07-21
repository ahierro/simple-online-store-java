package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserDAOImplTest extends PostgresIntegrationSetup {

    @Container
    protected static PostgreSQLContainer<?> postgresqlContainer = createContainer();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static {
        init(postgresqlContainer);
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer, registry);
    }

    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private UserRepository userRepository;

    AppUserDomain exampleUser = null;

    public void deleteAllEntities() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @BeforeEach
    public void setup() {
        deleteAllEntities();

        exampleUser = AppUserDomain.builder()
                .id(UUID.fromString("9ea1e0f8-27ad-4915-9b49-9845b51f06d4"))
                .username("testuser")
                .password("encodedPassword123")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .active(false)
                .locked(false)
                .authorities(List.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Create a user and find it to verify if it is persisted in the database")
    void create_ok() {
        userDAO.create(exampleUser);
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    @DisplayName("Create a user that is already persisted in the database")
    void create_with_existing_username() {
        userDAO.create(exampleUser);
        assertEquals(1, userRepository.findAll().size());

        try {
            userDAO.create(exampleUser);
            fail("Expected Conflict exception was not thrown");
        } catch (Conflict e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Update an existing user")
    void update_ok() {
        create_ok();
        assertNotNull(exampleUser.getId());

        exampleUser.setFirstName("Jane");
        exampleUser.setLastName("Smith");
        exampleUser.setActive(true);

        userDAO.update(exampleUser);
        var user = userRepository.findById(exampleUser.getId()).orElse(null);
        assertNotNull(user);
        assertTrue(isEquals(exampleUser, user));
    }

    private boolean isEquals(AppUserDomain userDomain, com.iron.tec.labs.ecommercejava.db.entities.AppUser user) {
        return userDomain.getId().equals(user.getId())
                && userDomain.getUsername().equals(user.getUsername())
                && userDomain.getEmail().equals(user.getEmail())
                && userDomain.getFirstName().equals(user.getFirstName())
                && userDomain.getLastName().equals(user.getLastName())
                && userDomain.isActive() == user.isActive()
                && userDomain.isLocked() == user.isLocked();
    }

    @Test
    @DisplayName("Get user by ID")
    void getById_ok() {
        create_ok();

        AppUserDomain user = userDAO.getById(exampleUser.getId());
        assertNotNull(user);
        assertEquals(exampleUser.getId(), user.getId());
        assertEquals(exampleUser.getUsername(), user.getUsername());
    }

    @Test
    @DisplayName("Get non-existing user by ID")
    void getById_notFound() {
        try {
            userDAO.getById(UUID.randomUUID());
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Get user by username")
    void getByUsername_ok() {
        create_ok();

        AppUserDomain user = userDAO.getByUsername(exampleUser.getUsername());
        assertNotNull(user);
        assertEquals(exampleUser.getUsername(), user.getUsername());
        assertEquals(exampleUser.getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Get non-existing user by username")
    void getByUsername_notFound() {
        try {
            userDAO.getByUsername("nonexistent");
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Find user by ID and active status - inactive user")
    void findByIdAndActive_inactive_ok() {
        create_ok();

        AppUserDomain user = userDAO.findByIdAndActive(exampleUser.getId(), false);
        assertNotNull(user);
        assertEquals(exampleUser.getId(), user.getId());
        assertFalse(user.isActive());
    }

    @Test
    @DisplayName("Find user by ID and active status - active user")
    void findByIdAndActive_active_ok() {
        create_ok();
        exampleUser.setActive(true);

        userDAO.update(exampleUser);
        AppUserDomain user = userDAO.findByIdAndActive(exampleUser.getId(), true);
        
        assertNotNull(user);
        assertEquals(exampleUser.getId(), user.getId());
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("Find user by ID and active status - not found")
    void findByIdAndActive_notFound() {
        create_ok();

        // Try to find active user when user is inactive
        try {
            userDAO.findByIdAndActive(exampleUser.getId(), true);
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Find non-existing user by ID and active status")
    void findByIdAndActive_userNotExists() {
        try {
            userDAO.findByIdAndActive(UUID.randomUUID(), false);
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }
}