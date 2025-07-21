package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import com.iron.tec.labs.ecommercejava.db.entities.*;
import com.iron.tec.labs.ecommercejava.db.repository.*;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.PurchaseOrderEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineViewToDomain;
import com.iron.tec.labs.ecommercejava.mappers.user.AppUserEntityToDomain;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@Transactional
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
    private PurchaseOrderLineRepository purchaseOrderLineRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PurchaseOrderEntityToDomain purchaseOrderEntityToDomain;

    PurchaseOrder purchaseOrder = null;
    Product product = null;
    Category category = null;
    AppUser appUser = null;

    @BeforeEach
    public void setup() {
        purchaseOrderEntityToDomain = new PurchaseOrderEntityToDomain(
                new PurchaseOrderLineEntityToDomain(new ProductEntityToDomain()),
                new PurchaseOrderLineViewToDomain(), new AppUserEntityToDomain());
        deleteAllEntities();
        appUser = AppUser.builder()
                .id(UUID.randomUUID())
                .username("admintest")
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
                .category(category)
                .build();

        purchaseOrder = PurchaseOrder.builder()
                .id(UUID.randomUUID())
                .user(appUser)
                .line(PurchaseOrderLine.builder().product(product).quantity(10).build())
                .status("PENDING")
                .total(BigDecimal.valueOf(100000))
                .build();

        userRepository.save(appUser);
        assertEquals(1, userRepository.findAll().size());

        categoryRepository.save(category);
        assertEquals(1, categoryRepository.findAll().size());

        productRepository.save(product);
        assertEquals(1, productRepository.findAll().size());
    }
    public void deleteAllEntities() {
        jdbcTemplate.update("DELETE FROM purchase_order_line");
        jdbcTemplate.update("DELETE FROM purchase_order");
        jdbcTemplate.update("DELETE FROM product");
        jdbcTemplate.update("DELETE FROM category");
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    @DisplayName("Create a PurchaseOrder and find it to verify if it is persisted in the database")
    void create_ok() {
        PurchaseOrderDomain purchaseOrderDomain = purchaseOrderEntityToDomain.convert(purchaseOrder);
        purchaseOrderDAO.create(purchaseOrderDomain);
        assertEquals(1, purchaseOrderRepository.findAll().size());
    }

    @Test
    @DisplayName("Create a PurchaseOrder that is already persisted in the database")
    void create_with_existing_id() {
        PurchaseOrderDomain purchaseOrderDomain = purchaseOrderEntityToDomain.convert(purchaseOrder);
        purchaseOrderDAO.create(purchaseOrderDomain);
        assertEquals(1, purchaseOrderRepository.findAll().size());
        
        try {
            purchaseOrderDAO.create(purchaseOrderDomain);
            fail("Expected Conflict exception was not thrown");
        } catch (Conflict e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Update an existing PurchaseOrder")
    void update_ok() {
        create_ok();
        assert purchaseOrder.getId() != null;
        PurchaseOrderDomain purchaseOrderDomain = purchaseOrderEntityToDomain.convert(Objects.requireNonNull(purchaseOrderRepository.findById(purchaseOrder.getId()).orElse(null)));
        purchaseOrderDAO.update(purchaseOrderDomain);
        assertEquals(1, purchaseOrderRepository.findAll().size());
    }

    @Test
    @DisplayName("Update non-existing PurchaseOrder")
    void update_nonExisting_error() {
        assert purchaseOrder.getId() != null;
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        PurchaseOrderDomain purchaseOrderDomain = purchaseOrderEntityToDomain.convert(purchaseOrder);
        try {
            purchaseOrderDAO.update(purchaseOrderDomain);
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Create PurchaseOrder and get a PurchaseOrder page to verify if it is returned")
    void getPage_ok() {
        create_ok();
        // Convert entity to domain for getPage
        PurchaseOrderDomain purchaseOrderDomain = purchaseOrderEntityToDomain.convert(purchaseOrder);
        PageDomain<PurchaseOrderDomain> page = purchaseOrderDAO.getPage(0, 2, purchaseOrderDomain);
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
        // Pass an empty domain object for filter
        PurchaseOrderDomain emptyDomain = PurchaseOrderDomain.builder().build();
        PageDomain<PurchaseOrderDomain> page = purchaseOrderDAO.getPage(0, 2, emptyDomain);
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
        PurchaseOrderDomain result = purchaseOrderDAO.getById(purchaseOrder.getId());
        assertNotNull(result);
    }

    @Test
    @DisplayName("Get non existing PurchaseOrder")
    void getById_notFound() {
        try {
            purchaseOrderDAO.getById(UUID.randomUUID());
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }
}
