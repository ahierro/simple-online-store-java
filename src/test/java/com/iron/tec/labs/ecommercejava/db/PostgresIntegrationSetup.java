package com.iron.tec.labs.ecommercejava.db;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresIntegrationSetup {

	@Container
	protected static PostgreSQLContainer<?> postgresqlContainer
			= new PostgreSQLContainer<>("postgres:15.1-alpine3.17");

	static {
		postgresqlContainer.withInitScript("schema.sql");
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgresqlContainer.getHost() + ":"
				+ postgresqlContainer.getFirstMappedPort() + "/" + postgresqlContainer.getDatabaseName());
		registry.add("spring.r2dbc.username", () -> "test");
		registry.add("spring.r2dbc.password", () -> "test");
	}

}