package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserDAOImplTest extends PostgresIntegrationSetup {

    @Container
    protected static PostgreSQLContainer<?> postgresqlContainer = createContainer();

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

    @BeforeEach
    public void setup() {
        StepVerifier.create(userRepository.deleteAll()).verifyComplete();

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
        StepVerifier.create(userDAO.create(exampleUser)
                .thenMany(userRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create a user that is already persisted in the database")
    void create_with_existing_username() {
        StepVerifier.create(userDAO.create(exampleUser)
                .thenMany(userRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(userDAO.create(exampleUser))
                .verifyError(Conflict.class);
    }

    @Test
    @DisplayName("Update an existing user")
    void update_ok() {
        create_ok();
        assertNotNull(exampleUser.getId());

        exampleUser.setFirstName("Jane");
        exampleUser.setLastName("Smith");
        exampleUser.setActive(true);

        StepVerifier.create(userDAO.update(exampleUser)
                .then(userRepository.findById(exampleUser.getId())))
                .expectNextMatches(user -> isEquals(exampleUser, user))
                .verifyComplete();
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

        StepVerifier.create(userDAO.getById(exampleUser.getId()))
                .expectNextMatches(user -> user.getId().equals(exampleUser.getId())
                        && user.getUsername().equals(exampleUser.getUsername()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Get non-existing user by ID")
    void getById_notFound() {
        StepVerifier.create(userDAO.getById(UUID.randomUUID()))
                .verifyError(NotFound.class);
    }

    @Test
    @DisplayName("Get user by username")
    void getByUsername_ok() {
        create_ok();

        StepVerifier.create(userDAO.getByUsername(exampleUser.getUsername()))
                .expectNextMatches(user -> user.getUsername().equals(exampleUser.getUsername())
                        && user.getEmail().equals(exampleUser.getEmail()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Get non-existing user by username")
    void getByUsername_notFound() {
        StepVerifier.create(userDAO.getByUsername("nonexistent"))
                .verifyError(NotFound.class);
    }

    @Test
    @DisplayName("Find user by ID and active status - inactive user")
    void findByIdAndActive_inactive_ok() {
        create_ok();

        StepVerifier.create(userDAO.findByIdAndActive(exampleUser.getId(), false))
                .expectNextMatches(user -> user.getId().equals(exampleUser.getId())
                        && !user.isActive())
                .verifyComplete();
    }

    @Test
    @DisplayName("Find user by ID and active status - active user")
    void findByIdAndActive_active_ok() {
        create_ok();
        exampleUser.setActive(true);

        StepVerifier.create(userDAO.update(exampleUser)
                .then(userDAO.findByIdAndActive(exampleUser.getId(), true)))
                .expectNextMatches(user -> user.getId().equals(exampleUser.getId())
                        && user.isActive())
                .verifyComplete();
    }

    @Test
    @DisplayName("Find user by ID and active status - not found")
    void findByIdAndActive_notFound() {
        create_ok();

        // Try to find active user when user is inactive
        StepVerifier.create(userDAO.findByIdAndActive(exampleUser.getId(), true))
                .verifyError(NotFound.class);
    }

    @Test
    @DisplayName("Find non-existing user by ID and active status")
    void findByIdAndActive_userNotExists() {
        StepVerifier.create(userDAO.findByIdAndActive(UUID.randomUUID(), false))
                .verifyError(NotFound.class);
    }
}