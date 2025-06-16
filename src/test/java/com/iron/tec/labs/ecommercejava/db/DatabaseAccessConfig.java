package com.iron.tec.labs.ecommercejava.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iron.tec.labs.ecommercejava.db.dao.CategoryDAOImpl;
import com.iron.tec.labs.ecommercejava.db.dao.ProductDAOImpl;
import com.iron.tec.labs.ecommercejava.db.dao.PurchaseOrderDAO;
import com.iron.tec.labs.ecommercejava.db.dao.PurchaseOrderDAOImpl;
import com.iron.tec.labs.ecommercejava.db.repository.*;
import com.iron.tec.labs.ecommercejava.db.rowmappers.ColumnConverter;
import com.iron.tec.labs.ecommercejava.db.rowmappers.ProductRowMapper;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@SpringBootApplication
//@EnableTransactionManagement
//@EnableR2dbcAuditing
//@EnableR2dbcRepositories
public class DatabaseAccessConfig {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseAccessConfig.class, args);
	}

	@Bean
	public ProductDAOImpl productDaoImpl(ProductRepository productRepository, MessageService messageService,
										 CustomProductRepository customProductRepository,
										 ProductViewRepository productViewRepository,
										 ConversionService conversionService){
		return new ProductDAOImpl(productRepository, messageService, customProductRepository, productViewRepository, conversionService);
	}
	@Bean
	public CategoryDAOImpl categoryImpl(CategoryRepository categoryRepository, MessageService messageService,
										ConversionService conversionService){
		return new CategoryDAOImpl(conversionService,categoryRepository,messageService);
	}
	@Bean
	public PurchaseOrderDAO purchaseOrderDAOImpl(PurchaseOrderRepository purchaseOrderRepository, MessageService messageService,
												 PurchaseOrderViewRepository purchaseOrderViewRepository,
												 PurchaseOrderLineViewRepository purchaseOrderLineViewRepository,
												 UserRepository appUserRepository){
		return new PurchaseOrderDAOImpl(purchaseOrderRepository,messageService,purchaseOrderViewRepository,
				purchaseOrderLineViewRepository,appUserRepository);
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

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
    }

}
