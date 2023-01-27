package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import com.iron.tec.labs.ecommercejava.db.entities.*;
import com.iron.tec.labs.ecommercejava.db.repository.CategoryRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderRepository;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.enums.PurchaseOrderStatus.CANCELLED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataR2dbcTest
@ComponentScan("com.iron.tec.labs.ecommercejava.config.db")
@TestMethodOrder(MethodOrderer.MethodName.class)
class PurchaseOrderDAOImplTest extends PostgresIntegrationSetup {

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
    private PurchaseOrderDAO purchaseOrderDAO;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    MessageService messageService;

    PurchaseOrder purchaseOrder = null;
    Product product = null;
    Category category = null;
    AppUser appUser = null;

    @BeforeEach
    public void setup() {
        StepVerifier.create(purchaseOrderRepository.deleteAll()).verifyComplete();
        StepVerifier.create(productRepository.deleteAll()).verifyComplete();
        StepVerifier.create(categoryRepository.deleteAll()).verifyComplete();
        StepVerifier.create(userRepository.deleteAll()).verifyComplete();

        appUser = AppUser.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .password("$2a$10$7EVF8hBxswNOWMPfpIImruKVkUbNcL51KK.TueUqUPjnfdAghhJmC")
                .firstName("John")
                .lastName("Smith")
                .email("admin@gmail.com")
                .active(true)
                .locked(false)
                .authority("ROLE_ADMIN")
                .build();

        category = Category.builder()
                .id(UUID.randomUUID())
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();

        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .idCategory(category.getId())
                .build();

        purchaseOrder = PurchaseOrder.builder()
                .id(UUID.randomUUID())
                .idUser(appUser.getId())
                .line(PurchaseOrderLine.builder().idProduct(product.getId()).quantity(10).build())
                .status("PENDING")
                .total(BigDecimal.valueOf(100000))
                .build();

        StepVerifier.create(userRepository.save(appUser)
                        .thenMany(userRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(categoryRepository.save(category)
                        .thenMany(categoryRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();


        StepVerifier.create(productRepository.save(product)
                        .thenMany(productRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create a PurchaseOrder and find it to verify if it is persisted in the database")
    void create_ok() {
        StepVerifier.create(purchaseOrderDAO.create(purchaseOrder)
                        .thenMany(purchaseOrderRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create a PurchaseOrder that is already persisted in the database")
    void create_with_existing_id() {
        StepVerifier.create(purchaseOrderDAO.create(purchaseOrder)
                        .thenMany(purchaseOrderRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();
        purchaseOrder.setCreatedAt(null);
        StepVerifier.create(purchaseOrderDAO.create(purchaseOrder)
                        .thenMany(purchaseOrderRepository.findAll()))
                .verifyError(Conflict.class);
    }

    @Test
    @DisplayName("Update an existing PurchaseOrder")
    void update_ok() {
        create_ok();
        assert purchaseOrder.getId() != null;
        purchaseOrder.setStatus("CANCELLED");
        assert purchaseOrder.getId() != null;
        StepVerifier.create(purchaseOrderDAO.update(purchaseOrder)
                        .then(purchaseOrderRepository.findById(purchaseOrder.getId())))
                .expectNextMatches(purchaseOrder -> {
                    assertNotNull(purchaseOrder);
                    assertEquals(CANCELLED.name(), purchaseOrder.getStatus());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Update non-existing PurchaseOrder")
    void update_nonExisting_error() {
        assert purchaseOrder.getId() != null;
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        StepVerifier.create(purchaseOrderDAO.update(purchaseOrder))
                .verifyError(NotFound.class);
    }

    @Test
    @DisplayName("Create PurchaseOrder and get a PurchaseOrder page to verify if it is returned")
    void getPage_ok() {
        create_ok();
        Page<PurchaseOrderView> page = purchaseOrderDAO.getPage(0, 2, PurchaseOrderView.builder().build()).block();
        assertNotNull(page);
        assertEquals(1, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertEquals(1, page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(1, page.getContent().size());
    }

    @Test
    @DisplayName("Get a PurchaseOrder page when the table is empty")
    void getPage_empty() {
        Page<PurchaseOrderView> page = purchaseOrderDAO.getPage(0, 2, PurchaseOrderView.builder().build()).block();
        assertNotNull(page);
        assertEquals(0, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertEquals(0, page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(0, page.getContent().size());
    }

    @Test
    @DisplayName("Create a PurchaseOrder and get PurchaseOrder by id to verify if it is returned")
    void getById_ok() {
        create_ok();
        StepVerifier.create(purchaseOrderDAO.getById(purchaseOrder.getId()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Get non existing PurchaseOrder")
    void getById_notFound() {
        StepVerifier.create(purchaseOrderDAO.getById(UUID.randomUUID()))
                .verifyError(NotFound.class);
    }
}
