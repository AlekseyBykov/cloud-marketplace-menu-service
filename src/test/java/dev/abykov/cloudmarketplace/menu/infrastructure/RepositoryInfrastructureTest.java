package dev.abykov.cloudmarketplace.menu.infrastructure;

import dev.abykov.cloudmarketplace.menu.repository.MenuItemRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Infrastructure integration tests for verifying that the persistence layer
 * is correctly configured in the {@code test} profile.
 *
 * <p>This test class ensures that:
 * <ul>
 *     <li>{@link DataSource} is initialized using properties from
 *         {@code application-test.yml}</li>
 *     <li>A valid connection to the PostgreSQL Testcontainers database can be established</li>
 *     <li>{@link EntityManager} is correctly configured and can execute SQL queries</li>
 *     <li>{@link MenuItemRepository} bean is created and operational</li>
 * </ul>
 *
 * <p>Configuration is loaded from:
 * <pre>
 * spring:
 *   datasource:
 *     url: jdbc:tc:postgresql:16-alpine:///test_db
 *     driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
 *     username: test
 *     password: test
 *
 *   jpa:
 *     hibernate:
 *       ddl-auto: create-drop
 *     properties:
 *       hibernate:
 *         dialect: org.hibernate.dialect.PostgreSQLDialect
 * </pre>
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryInfrastructureTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MenuItemRepository repository;

    /**
     * Verifies that the {@link DataSource} bean is successfully created
     * using configuration from {@code application-test.yml}.
     */
    @Test
    @DisplayName("DataSource should be created from test configuration")
    void dataSourceShouldBeInitialized() {
        assertThat(dataSource).isNotNull();
    }

    /**
     * Ensures that a valid connection to the PostgreSQL database can be obtained.
     * The database is started automatically via Testcontainers using the
     * {@code jdbc:tc:postgresql} URL.
     */
    @Test
    @DisplayName("DataSource should provide a valid PostgreSQL connection")
    void dataSourceShouldProvideValidConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(2)).isTrue();
            assertThat(connection.getMetaData().getDatabaseProductName())
                    .containsIgnoringCase("PostgreSQL");
        }
    }

    /**
     * Verifies that the {@link EntityManager} is correctly configured
     * and capable of executing native SQL queries.
     */
    @Test
    @DisplayName("EntityManager should execute native queries")
    void entityManagerShouldExecuteNativeQuery() {
        Object result = entityManager
                .createNativeQuery("SELECT 1")
                .getSingleResult();

        assertThat(result).isNotNull();
        assertThat(((Number) result).intValue()).isEqualTo(1);
    }

    /**
     * Ensures that the {@link MenuItemRepository} bean is successfully
     * created and available in the Spring context.
     */
    @Test
    @DisplayName("MenuItemRepository should be created")
    void repositoryShouldBeInitialized() {
        assertThat(repository).isNotNull();
    }

    /**
     * Verifies that the repository can interact with the database.
     * Since Hibernate is configured with {@code ddl-auto=create-drop},
     * the schema is created automatically and initially contains no data.
     */
    @Test
    @DisplayName("MenuItemRepository should interact with the database")
    void repositoryShouldInteractWithDatabase() {
        long count = repository.count();
        assertThat(count).isZero();
    }
}
