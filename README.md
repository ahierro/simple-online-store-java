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

### Create public and private keys to sign and verify JWT tokens
* cd src/main/resources/cert
* openssl genrsa -out keypair.pem 2048
* openssl rsa -in keypair.pem -pubout -out public.pem
* openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
* delete keypair.pem

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

### License
This project is licensed under the [MIT License](LICENSE).
