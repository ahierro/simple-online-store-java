###  Local PostgreSQL can be run with the following command
docker run --detach --name postgres --env POSTGRES_PASSWORD=postgres --publish 5432:5432 postgres

###  To initialize Database this must be run before running
./mvnw liquibase:update

./mvnw liquibase:generateChangeLog -D liquibase.outputChangeLogFile=./src/main/resources/db/changelog/generated.sql

### Swagger documentation can be found at 
http://localhost:8080/swagger-ui.html
