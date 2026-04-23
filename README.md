# Cloud Marketplace Menu Service

## Overview

**Cloud Marketplace Menu Service** is a RESTful microservice responsible for managing menu items within a cloud-based
marketplace system.

The service provides CRUD operations, validation, sorting, and structured error handling, and is designed as part of a
larger microservices ecosystem.

## Architecture

The service follows a layered architecture:

```
Controller → Service → Repository → Database
```

### Key components:

* **Controller layer** — REST API endpoints
* **Service layer** — business logic and orchestration
* **Repository layer** — data access via Spring Data JPA
* **Database** — PostgreSQL with JSONB support

## Tech Stack

* **Java 21**
* **Spring Boot 3**

    * Spring Web
    * Spring Data JPA
    * Spring Validation
    * Spring Actuator
* **Hibernate 6**
* **PostgreSQL 16**
* **Flyway** (database migrations)
* **MapStruct** (DTO mapping)
* **Lombok**
* **Hypersistence Utils** (JSONB support)
* **Testcontainers** (integration testing)
* **Docker Compose**
* **OpenAPI (springdoc)**

## Features

* CRUD operations for menu items
* Category-based filtering
* Sorting (price, name, created date)
* JSONB-based attributes storage
* Request validation (Bean Validation + custom constraints)
* Centralized exception handling (RFC 7807 / ProblemDetail)
* Integration testing with real PostgreSQL (Testcontainers)

## Running the Application

### 1. Start PostgreSQL via Docker

```bash
docker-compose up -d
```

### 2. Run the application

```bash
./gradlew bootRun
```

### 3. Application will be available at:

```
http://localhost:8080
```

## Running Tests

### Run all tests:

```bash
./gradlew test
```

### Integration tests use Testcontainers:

* Docker must be running
* PostgreSQL container will be started automatically

## Example API

### Get menu items by category

```http
GET /api/menu-items?category=MAIN_DISH&sort=price_asc
```

## Error Handling

The service uses **RFC 7807 (Problem Details for HTTP APIs)**.

Example response:

```json
{
  "status": 400,
  "detail": "Validation failed",
  "invalid_params": {
    "name": "must not be blank"
  }
}
```

## Docker

PostgreSQL is configured via `docker-compose.yml`.

Default port:

```
15432 → 5432 (container)
```

## License

MIT License
