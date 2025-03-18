package com.iron.tec.labs.ecommercejava;

import com.iron.tec.labs.ecommercejava.config.security.RsaKeyProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.iron.tec.labs.ecommercejava")
@EnableTransactionManagement
@EnableR2dbcAuditing
@EnableConfigurationProperties(RsaKeyProperties.class)
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
