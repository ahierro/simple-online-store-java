### Project Purpose
This project was build to be part of my personal portfolio. It is just a sample code that is not intended to be used in production.
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
docker run --detach --name postgres --env POSTGRES_PASSWORD=postgres --publish 5432:5432 postgres
``` 
### Initialize Database this must be run before running
```shell
./mvnw liquibase:update
```
### (In case it is needed) Rollback last changeset
```shell
./mvnw liquibase:rollback "-Dliquibase.rollbackCount=1"
```
### The following SQL statements can be used to initialize users table for testing purposes
```sql
INSERT INTO public.users (id, username, password, email, first_name, last_name, active, locked, authorities, created_at) VALUES ('b95c3787-1194-4a3e-a1dc-3cf41e23b77b', 'alejandrohierro', '$2a$10$x4bdKaA3brH9qQAkQCo0pOWEUJgvK7c/2HnPrcOra4pmA0b1oFgca', 'alejandrohierro@gmail.com', 'Alejandro', 'Hierro', true, false, '["ROLE_USER"]', '2023-01-06 18:46:48.857729');

INSERT INTO public.users (id, username, password, email, first_name, last_name, active, locked, authorities, created_at) VALUES ('130b1b88-5850-4d25-b81f-786925d09ab7', 'admin', '$2a$10$7EVF8hBxswNOWMPfpIImruKVkUbNcL51KK.TueUqUPjnfdAghhJmC', 'admin@gmail.com', 'Alejandro', 'Admin', true, false, '["ROLE_ADMIN"]', '2023-01-06 18:47:26.046147');
```

### Postman collection file
simple-online-store-java.postman_collection.json

### Docker image building
```shell
./mvnw spring-boot:build-image
``` 
### Docker Run

```shell
docker run -d --add-host host.docker.internal:host-gateway --name ecommerce-java -p 8080:8080 ecommerce-java:0.0.1-SNAPSHOT
``` 