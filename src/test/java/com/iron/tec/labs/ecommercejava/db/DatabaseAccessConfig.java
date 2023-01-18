package com.iron.tec.labs.ecommercejava.db;

import com.iron.tec.labs.ecommercejava.db.dao.CategoryDAOImpl;
import com.iron.tec.labs.ecommercejava.db.dao.ProductDAOImpl;
import com.iron.tec.labs.ecommercejava.db.repository.*;
import com.iron.tec.labs.ecommercejava.db.rowmappers.ColumnConverter;
import com.iron.tec.labs.ecommercejava.db.rowmappers.ProductRowMapper;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableR2dbcAuditing
@EnableR2dbcRepositories
public class DatabaseAccessConfig {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseAccessConfig.class, args);
	}

	@Bean
	public ProductDAOImpl productDaoImpl(ProductRepository productRepository, MessageService messageService,
										 CustomProductRepository customProductRepository,
										 ProductViewRepository productViewRepository){
		return new ProductDAOImpl(productRepository,messageService,customProductRepository,productViewRepository);
	}
	@Bean
	public CategoryDAOImpl categoryImpl(CategoryRepository categoryRepository, MessageService messageService){
		return new CategoryDAOImpl(categoryRepository,messageService);
	}

	@Bean
	public CustomProductRepository CustomProductRepositoryImpl(DatabaseClient db, ProductRowMapper productRowMapper){
		return new CustomProductRepositoryImpl(db,productRowMapper);
	}

	@Bean
	public ProductRowMapper productRowMapper(ColumnConverter converter){
		return new ProductRowMapper(converter);
	}

	@Bean
	public ColumnConverter columnConverter(R2dbcCustomConversions conversions, R2dbcConverter r2dbcConverter){
		return new ColumnConverter(conversions,r2dbcConverter);
	}

}
