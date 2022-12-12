docker run --detach --name postgres --env POSTGRES_PASSWORD=postgres --publish 5432:5432 postgres

http://localhost:8080/swagger-ui.html

docker run --detach --name postgres-prod --env POSTGRES_PASSWORD=prod --publish 5432:5432 postgres