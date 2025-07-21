package com.iron.tec.labs.ecommercejava;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.iron.tec.labs.ecommercejava")
@EnableTransactionManagement
@EnableJpaAuditing
@EnableAsync
@OpenAPIDefinition(info=@Info(title="Ecommerce Java", version="1.0"))
public class EcommerceJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceJavaApplication.class, args);
	}

	@PostConstruct
	public void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
	}

}
