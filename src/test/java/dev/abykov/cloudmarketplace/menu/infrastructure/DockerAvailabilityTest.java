package dev.abykov.cloudmarketplace.menu.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Infrastructure test that verifies Docker and Testcontainers availability.
 *
 * <p>This test ensures that:
 * <ul>
 *     <li>Docker daemon is accessible from the test environment</li>
 *     <li>Testcontainers can successfully start a PostgreSQL container</li>
 *     <li>The container reaches the running state</li>
 * </ul>
 *
 * <p>The test uses a lightweight PostgreSQL image:
 * <pre>
 * postgres:16-alpine
 * </pre>
 *
 * <p>Configuration of the container:
 * <ul>
 *     <li>Database name: {@code test_db}</li>
 *     <li>Username: {@code test}</li>
 *     <li>Password: {@code test}</li>
 * </ul>
 *
 * <p>This test is especially useful in CI/CD environments to quickly
 * detect issues related to Docker availability, VPN restrictions,
 * or insufficient permissions to access {@code /var/run/docker.sock}.
 *
 * <p>Note: If the test fails with errors such as
 * {@code DockerClientProviderStrategy} or {@code RyukResourceReaper},
 * ensure that:
 * <ul>
 *     <li>Docker is running</li>
 *     <li>The current user belongs to the {@code docker} group</li>
 *     <li>VPN does not block container networking</li>
 * </ul>
 */
@Testcontainers
class DockerAvailabilityTest {

    /**
     * PostgreSQL container managed by Testcontainers.
     * The container lifecycle is controlled automatically by the
     * {@link Testcontainers} extension.
     */
    @Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("test_db")
                    .withUsername("test")
                    .withPassword("test");

    /**
     * Verifies that Docker is available and the PostgreSQL container
     * has been successfully started.
     */
    @Test
    @DisplayName("Docker should be available and PostgreSQL container should be running")
    void shouldStartPostgresContainer() {
        assertThat(postgres.isRunning())
                .as("PostgreSQL Testcontainer should be running")
                .isTrue();
    }

    /**
     * Ensures that the container exposes a valid JDBC URL.
     * This confirms that the database is ready to accept connections.
     */
    @Test
    @DisplayName("PostgreSQL container should expose valid JDBC configuration")
    void shouldExposeValidJdbcConfiguration() {
        assertThat(postgres.getJdbcUrl()).isNotBlank();
        assertThat(postgres.getUsername()).isEqualTo("test");
        assertThat(postgres.getPassword()).isEqualTo("test");
    }

    /**
     * Verifies that the container is created from the expected Docker image.
     * This helps ensure consistency across different environments.
     */
    @Test
    @DisplayName("PostgreSQL container should use the expected Docker image")
    void shouldUseExpectedDockerImage() {
        assertThat(postgres.getDockerImageName())
                .contains("postgres:16-alpine");
    }
}
