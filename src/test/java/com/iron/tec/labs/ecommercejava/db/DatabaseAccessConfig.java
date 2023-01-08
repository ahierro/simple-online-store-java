package com.iron.tec.labs.ecommercejava.db;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAOImpl;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import com.iron.tec.labs.ecommercejava.services.MessageServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableR2dbcAuditing
public class DatabaseAccessConfig {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseAccessConfig.class, args);
	}

	@Bean
	public ProductDAOImpl productDaoImpl(ProductRepository productRepository, MessageService messageService){
		return new ProductDAOImpl(productRepository,messageService);
	}

}
