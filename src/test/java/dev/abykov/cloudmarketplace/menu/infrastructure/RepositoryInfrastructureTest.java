package dev.abykov.cloudmarketplace.menu.infrastructure;

import dev.abykov.cloudmarketplace.menu.repository.MenuItemRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryInfrastructureTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    void contextLoads_andInfrastructureIsReady() throws Exception {
        // Проверяем, что DataSource создан
        assertThat(dataSource).isNotNull();

        // Проверяем соединение с PostgreSQL
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(2)).isTrue();
            assertThat(connection.getMetaData().getDatabaseProductName())
                    .containsIgnoringCase("PostgreSQL");
        }

        // Проверяем работу EntityManager через native query
        Object result = entityManager
                .createNativeQuery("SELECT 1")
                .getSingleResult();
        assertThat(result).isNotNull();

        // Проверяем, что репозиторий создан
        assertThat(menuItemRepository).isNotNull();

        // Дополнительная проверка: репозиторий может выполнять запросы
        long count = menuItemRepository.count();
        assertThat(count).isZero();
    }
}
