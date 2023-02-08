package com.iron.tec.labs.ecommercejava;

import com.iron.tec.labs.ecommercejava.config.security.RsaKeyProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;
import java.util.TimeZone;

@SpringBootApplication
@EnableTransactionManagement
@EnableR2dbcAuditing
@EnableConfigurationProperties(RsaKeyProperties.class)
public class EcommerceJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceJavaApplication.class, args);
	}

	@PostConstruct
	public void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
	}

}
