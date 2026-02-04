package com.iron.tec.labs.ecommercejava.db;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
public class PostgresIntegrationSetup {

    public static PostgreSQLContainer createContainer() {
        return new PostgreSQLContainer("postgres:18.1-alpine3.23");
    }

    public static void init(PostgreSQLContainer postgresqlContainer) {
        postgresqlContainer.withInitScript("db/changelog/changelog.sql");
    }
    public static void initWithScripts(PostgreSQLContainer postgresqlContainer,String path) {
        postgresqlContainer.withInitScripts("db/changelog/changelog.sql",path);
    }
    public static void overrideProperties(PostgreSQLContainer postgresqlContainer, DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgresqlContainer.getHost() + ":"
                + postgresqlContainer.getFirstMappedPort() + "/" + postgresqlContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", () -> "test");
        registry.add("spring.r2dbc.password", () -> "test");
    }
}