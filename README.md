# user-data-proxy
Simple REST proxy fetching user data from github API, keeping simultaneously track of fetching stats for particular users.

## Launching
To run database:
```
docker-compose up
```
To run app:
```
mvnw spring-boot:run
```
If you don't want to run docker db container, create schema "user_proxy" beforehand and make sure postgres has default db name and credentials.

## API
Swagger can be found under http://localhost:8080/
