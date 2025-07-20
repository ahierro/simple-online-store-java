[![Actions Status](https://github.com/ahierro/simple-online-store-java/actions/workflows/maven.yml/badge.svg)](https://github.com/ahierro/simple-online-store-java/actions)
[![codecov](https://codecov.io/github/ahierro/simple-online-store-java/graph/badge.svg?token=011U7VD234)](https://codecov.io/github/ahierro/simple-online-store-java)
### Project Purpose
This project was build to be part of my personal portfolio. It is just a sample code not intended to be used in production.
This Spring Boot application provides a REST API for an oversimplified ecommerce website without integration with payment or delivery methods.
It includes CRUD operations for Product, Category, User, PurchaseOrder and PurchaseOrderLine entities. It also includes a simple authentication mechanism using JWT.

### Technologies used
* Java 21
* Spring Boot 3
* Spring Webflux
* Spring Security
* Liquibase
* PostgreSQL
* Spring Data R2DBC (including queries with pagination and sorting)
* Test Containers
* Swagger
* JWT authentication
* Junit
* Lombok
* Maven

### Swagger documentation can be found at 
http://localhost:8080/swagger-ui.html
![swagger](/swagger.jpg "Swagger")

### Environment Configuration

Create a `.env` file in the project root directory to configure the application. Copy the template below and adjust the values according to your environment:

```env
# Database Configuration
DB_URL=r2dbc:postgresql://host.docker.internal:5432/postgres
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Logging Configuration
LOG_R2DBC_QUERY=DEBUG
INCLUDE_STACKTRACE=never
LOG_HTTP_WEB_HANDLER=DEBUG
LOG_ECOMMERCE=DEBUG

# Schema Initialization
INITIALIZE_SCHEMA=false

# JWT Authentication Keys
RSA_PRIVATE_KEY=classpath:certs/private.pem
RSA_PUBLIC_KEY=classpath:certs/public.pem

# Internationalization
MESSAGES_BASENAME=i18n/messages

# Email Configuration (SMTP)
MAIL_HOST=smtp.ethereal.email
MAIL_PORT=587
MAIL_USERNAME=your-email@ethereal.email
MAIL_PASSWORD=your-email-password
MAIL_PROTOCOL=smtp
MAIL_AUTH=true
MAIL_START_TLS=true
MAIL_DEBUG=true

# Docker Compose
DOCKER_COMPOSE_ENABLED=false

# Application Configuration
APP_BASE_URL=http://localhost:8080
```

#### Environment Variables Explained

**Database Configuration:**
- `DB_URL`: R2DBC connection URL for PostgreSQL database
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password

**Logging Configuration:**
- `LOG_R2DBC_QUERY`: Log level for R2DBC SQL queries (DEBUG, INFO, WARN, ERROR)
- `INCLUDE_STACKTRACE`: Whether to include stack traces in error responses (never, always, on_param)
- `LOG_HTTP_WEB_HANDLER`: Log level for HTTP web handler adapter
- `LOG_ECOMMERCE`: Log level for the ecommerce application package

**Schema Configuration:**
- `INITIALIZE_SCHEMA`: Whether to initialize database schema on startup (true/false)

**JWT Authentication:**
- `RSA_PRIVATE_KEY`: Path to RSA private key file for JWT signing
- `RSA_PUBLIC_KEY`: Path to RSA public key file for JWT verification

**Internationalization:**
- `MESSAGES_BASENAME`: Base name for message resource bundles

**Email Configuration:**
- `MAIL_HOST`: SMTP server hostname
- `MAIL_PORT`: SMTP server port (typically 587 for TLS, 465 for SSL, 25 for plain)
- `MAIL_USERNAME`: SMTP authentication username
- `MAIL_PASSWORD`: SMTP authentication password
- `MAIL_PROTOCOL`: Mail transport protocol (smtp)
- `MAIL_AUTH`: Enable SMTP authentication (true/false)
- `MAIL_START_TLS`: Enable TLS encryption (true/false)
- `MAIL_DEBUG`: Enable mail debug logging (true/false)

**Docker Configuration:**
- `DOCKER_COMPOSE_ENABLED`: Enable Docker Compose integration (true/false)

**Application Configuration:**
- `APP_BASE_URL`: Base URL of the application for generating links

Note: The `.env` file is ignored by Git to keep sensitive information out of version control.

### Generate public and private keys for JWT tokens
Use the provided shell script to generate the necessary keys:
```shell
# Make the script executable
chmod +x generate-jwt-keys.sh

# Run the script
./generate-jwt-keys.sh
```

This will create the required public and private keys in the `src/main/resources/certs` directory.

### Local PostgreSQL instance can be started with Docker using the following command
```shell
docker run --detach --name postgres --env POSTGRES_PASSWORD=postgres:17.2 --publish 5432:5432 postgres
``` 
### Initialize Database this must be run before running
```shell
./mvnw liquibase:update
```
### (In case it is needed) Rollback last changeset
```shell
./mvnw liquibase:rollback "-Dliquibase.rollbackCount=1"
```

### Postman collection file
simple-online-store-java.postman_collection.json

### Run Locally (Postgres database runs automatically from docker compose)
```shell
./mvnw spring-boot:run -Dspring-boot.run.profiles=local 
```
### Docker image building
```shell
./mvnw spring-boot:build-image
``` 
### Docker Run

```shell
docker run -d --add-host host.docker.internal:host-gateway --name ecommerce-java -p 8080:8080 ecommerce-java:0.0.2-SNAPSHOT
``` 
### Test Coverage Report
```shell
mvn clean test jacoco:report
```

### Project Structure

```
src/
├── main/
│   ├── java/                                   # Java source files
│   │   └── com/iron/tec/labs/ecommercejava/
│   │       ├── config/                         # Configuration classes
│   │       ├── controllers/                    # REST API controllers
│   │       ├── db/                             # Database related code
│   │       │   ├── dao/                        # Data Access Objects
│   │       │   ├── entities/                   # Database entities
│   │       │   └── repositories/               # Spring Data repositories
│   │       ├── domain/                         # Domain models
│   │       ├── dto/                            # Data Transfer Objects
│   │       ├── exceptions/                     # Custom exceptions
│   │       ├── security/                       # Security related classes
│   │       ├── services/                       # Business logic services
│   │       └── utils/                          # Utility classes
│   └── resources/
│       ├── certs/                              # JWT certificates
│       ├── db/                                 # Database migration scripts
│       └── i18n/                               # Internationalization files
└── test/                                       # Test files
    ├── java/
    └── resources/
```

### API Endpoints

The application exposes the following main endpoints:

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|--------------|
| `/api/auth/signup` | POST | Register a new user | No |
| `/api/auth/login` | POST | Authenticate and get JWT | No |
| `/api/auth/confirm` | GET | Confirm user email | No |
| `/api/categories` | GET | List all categories | Yes |
| `/api/categories` | POST | Create a new category | Yes |
| `/api/categories/{id}` | GET | Get category by ID | Yes |
| `/api/categories/{id}` | PUT | Update a category | Yes |
| `/api/categories/{id}` | DELETE | Delete a category | Yes |
| `/api/products` | GET | List all products | Yes |
| `/api/products` | POST | Create a new product | Yes |
| `/api/products/{id}` | GET | Get product by ID | Yes |
| `/api/products/{id}` | PUT | Update a product | Yes |
| `/api/products/{id}` | DELETE | Delete a product | Yes |
| `/api/purchase-orders` | GET | List all purchase orders | Yes |
| `/api/purchase-orders` | POST | Create a new purchase order | Yes |
| `/api/purchase-orders/{id}` | GET | Get purchase order by ID | Yes |
| `/api/purchase-orders/{id}` | PATCH | Update purchase order status | Yes |

For detailed API documentation, please refer to the Swagger UI at `/swagger-ui.html` when the application is running.

### Architecture Decisions

This project follows Clean Architecture principles with some adaptations for the Spring ecosystem. The architecture is organized into distinct layers, each with a specific responsibility:

#### Core Architecture Layers

1. **Domain Layer**
   - Located in the `domain` package
   - Contains business entities and core business rules
   - Independent of other layers and frameworks
   - Represents the heart of the application with pure business logic

2. **Application/Service Layer**
   - Located in the `services` package
   - Implements use cases by orchestrating domain objects
   - Contains business workflows and application-specific logic
   - Depends on the domain layer but is independent of infrastructure concerns

3. **Interface Adapters Layer**
   - Controllers (`controllers` package): Handle HTTP requests/responses and transform DTOs to domain objects
   - Data Transfer Objects (`dto` package): Define the data structures for API input/output
   - Data Access (`db` package): Contains repositories and DAOs that implement persistence operations

4. **Infrastructure Layer**
   - Configuration (`config` package): Spring configuration classes
   - Security (`security` package): Authentication and authorization mechanisms
   - Utils (`utils` package): Cross-cutting utilities and helpers

#### Design Decisions

1. **Reactive Programming**
   - Application uses Spring WebFlux and reactive programming model with Reactor
   - All operations are non-blocking to improve scalability and resource utilization
   - Services return `Mono` or `Flux` types to enable reactive data processing

2. **Database Access**
   - R2DBC is used for reactive database access
   - DAOs abstract database operations from the service layer
   - Repositories extend Spring Data R2DBC interfaces for common operations

3. **Separation of Concerns**
   - Domain objects are distinct from database entities
   - DTOs are used at API boundaries to isolate domain model changes
   - Mapping between layers is handled explicitly to maintain clear boundaries

4. **Security Implementation**
   - JWT-based authentication with RSA public/private key signing
   - Role-based authorization for API endpoints
   - Email confirmation flow for user registration

These architecture decisions promote maintainability, testability, and a clear separation of concerns, making the codebase more resilient to change and easier to understand.

### License
This project is licensed under the [MIT License](LICENSE).
