# Cloud Marketplace Menu Service

## Overview

Cloud Marketplace Menu Service is a RESTful microservice responsible for managing menu items within a cloud-based marketplace system.

The service provides CRUD operations, validation, sorting, and structured error handling. It is designed as part of a larger microservices ecosystem.

## Architecture

The service follows a layered architecture:

```
Controller → Service → Repository → Database
```

### Components

* Controller — REST API endpoints
* Service — business logic
* Repository — data access (Spring Data JPA)
* Database — PostgreSQL (JSONB support)

## Tech Stack

* Java 21
* Spring Boot 3 (Web, Data JPA, Validation, Actuator)
* Hibernate 6
* PostgreSQL 16
* Flyway
* MapStruct
* Lombok
* Hypersistence Utils (JSONB)
* Docker Compose
* OpenAPI (springdoc)

Note: Testcontainers is present in the project but is not used for application tests. It is only used in isolated infrastructure checks.

## Features

* CRUD operations for menu items
* Category filtering
* Sorting (price, name, creation date)
* JSONB attributes storage
* Request validation
* Centralized error handling (RFC 7807 / ProblemDetail)

## Running the Application

### Start PostgreSQL

```
docker-compose up -d
```

### Run the application

```
./gradlew bootRun
```

Application will be available at:

```
http://localhost:8080
```

## Testing

The project uses a layered testing approach with different levels of isolation.

### Controller tests

Location: `controller/MenuItemControllerTest`

* Uses MockMvc
* Tests request mapping, validation, and exception handling
* Does not involve database or real services

```
HTTP (mock) → Controller → mock Service
```

### Integration tests (API)

Location: `controller/MenuItemControllerIT`

* Uses WebTestClient
* Runs full application context
* Executes real HTTP requests

```
HTTP → Controller → Service → Repository → PostgreSQL
```

### Service tests

Location: `service/MenuItemServiceTest`

* Uses SpringBootTest
* Executes business logic against real database
* Test data managed via SQL scripts

### Repository tests

Location: `repository/MenuItemRepositoryTest`

* Uses DataJpaTest
* Verifies custom queries, sorting, and update logic

### Infrastructure tests

* DockerAvailabilityTest — checks Docker and Testcontainers availability
* RepositoryInfrastructureTest — validates datasource and persistence configuration

These tests are isolated and do not affect the main test flow.

## Test Database

Tests use a PostgreSQL instance started via Docker Compose:

```
localhost:15432
```

Test data is managed using SQL scripts:

* insert-menu-items.sql
* clear-menu-items.sql

## Running Tests

### Start database

```
docker-compose up -d
```

### Run all tests

```
./gradlew test
```

### Run specific groups

Controller tests:

```
./gradlew test --tests "*MenuItemControllerTest"
```

Integration tests:

```
./gradlew test --tests "*MenuItemControllerIT"
```

Service tests:

```
./gradlew test --tests "*ServiceTest"
```

Repository tests:

```
./gradlew test --tests "*RepositoryTest"
```

## Notes

* Docker must be running
* PostgreSQL must be available on localhost:15432
* Integration, service, and repository tests depend on this database
* Testcontainers is not used for application tests

## Example API

```
GET /api/menu-items?category=DRINKS&sort=price_asc
```

## Error Handling

The service follows RFC 7807 (Problem Details).

Example response:

```
{
  "status": 400,
  "detail": "Validation failed",
  "invalid_params": {
    "name": "must not be blank"
  }
}
```

## Docker

PostgreSQL is configured via docker-compose.yml

```
15432 → 5432
```

## License

MIT License
