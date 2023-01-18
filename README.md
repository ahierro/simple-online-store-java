###  Local PostgreSQL can be run with the following command
docker run --detach --name postgres --env POSTGRES_PASSWORD=postgres --publish 5432:5432 postgres

###  To initialize Database this must be run before running
./mvnw liquibase:update

### Swagger documentation can be found at 
http://localhost:8080/swagger-ui.html

### Create public and private keys
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
delete keypair.pem