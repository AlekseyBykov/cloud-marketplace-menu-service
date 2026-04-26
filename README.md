## Cloud Marketplace Menu Service

### Overview

Cloud Marketplace Menu Service is a RESTful microservice responsible for managing menu items within a cloud-based
marketplace system.

The service provides CRUD operations, validation, sorting, and structured error handling. It is designed as part of a
larger microservices ecosystem.

### Architecture

The service follows a layered architecture:

```
Controller → Service → Repository → Database
```

#### Components

* Controller — REST API endpoints
* Service — business logic
* Repository — data access (Spring Data JPA)
* Database — PostgreSQL (JSONB support)

### Tech Stack

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

Note: Testcontainers is present in the project but is not used for application tests. It is only used in isolated
infrastructure checks.

### Features

* CRUD operations for menu items
* Category filtering
* Sorting (price, name, creation date)
* JSONB attributes storage
* Request validation
* Centralized error handling (RFC 7807 / ProblemDetail)

### Running the Application

There are two ways to run the service.

**1. Local development mode**

Runs the application directly on the host:

```
docker compose up -d
./gradlew bootRun
docker compose down
```

- Application runs on `localhost:8080`
- Uses PostgreSQL from Docker

**2. Docker mode**

Runs the full application stack in containers:

```
./gradlew clean build -x test
docker build -t menu-service .

cd docker
docker compose up -d
docker logs -f menu-service
docker compose down
```

- Application runs on `localhost:9091`
- Uses internal Docker network (`postgres` hostname)

**Notes**

```
jar {
    enabled = false
}
```

- this disables plain JAR generation and ensures only a Spring Boot fat JAR is produced.

`docker build -t menu-service .` - the project uses a multi-stage Dockerfile with layered JAR support to optimize build
caching and reduce rebuild time.

**Example API**

```
GET /api/menu-items?category=DRINKS&sort=price_asc
```

Example request:

```
curl http://localhost:9091/api/menu-items?category=drinks
```

Example response:

```
[
  {
    "id": 1,
    "name": "Latte",
    "description": "Coffee",
    "price": 5.00,
    "category": "drinks",
    "preparationTime": 120,
    "weight": 200.0,
    "imageUrl": "https://images.cloudmarketplace.dev/latte.png",
    "createdAt": "2026-04-23T16:02:17.18984",
    "updatedAt": "2026-04-23T16:02:17.189941",
    "attributes": {
      "attributes": [
        { "name": "Milk", "calories": 100 },
        { "name": "Coffee", "calories": 50 }
      ]
    }
  }
]
```

Returns all menu items in the "drinks" category sorted by default (name_asc).

**Error Handling**

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

### Testing

The project uses a layered testing approach with different levels of isolation.

**Controller tests**

Location: `controller/MenuItemControllerTest`

* Uses MockMvc
* Tests request mapping, validation, and exception handling
* Does not involve database or real services

```
HTTP (mock) → Controller → mock Service
```

**Integration tests (API)**

Location: `controller/MenuItemControllerIT`

* Uses WebTestClient
* Runs full application context
* Executes real HTTP requests

```
HTTP → Controller → Service → Repository → PostgreSQL
```

**Service tests**

Location: `service/MenuItemServiceTest`

* Uses SpringBootTest
* Executes business logic against real database
* Test data managed via SQL scripts

**Repository tests**

Location: `repository/MenuItemRepositoryTest`

* Uses DataJpaTest
* Verifies custom queries, sorting, and update logic

**Infrastructure tests**

* DockerAvailabilityTest — checks Docker and Testcontainers availability
* RepositoryInfrastructureTest — validates datasource and persistence configuration

These tests are isolated and do not affect the main test flow.

Tests use a PostgreSQL instance started via Docker Compose:

```
localhost:15432
```

Test data is managed using SQL scripts:

* insert-menu-items.sql
* clear-menu-items.sql

### Running Tests

**Start database**

```
docker-compose up -d
```

**Run all tests**

```
./gradlew test
```

**Run specific groups**

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

### Docker Optimization Notes

The Docker image is built using a multi-stage approach:

- First stage extracts Spring Boot layered JAR
- Second stage assembles runtime image using separated layers

This allows Docker to reuse cached layers when dependencies do not change, significantly speeding up rebuilds.

### Notes on Docker setup

- The service runs inside Docker on port 9091
- PostgreSQL is available via container hostname postgres
- `SPRING_DATASOURCE_URL` is overridden for container networking
- Docker Compose can either: use a pre-built image (`image:`) or build it automatically (`build:`)

### License

MIT License
