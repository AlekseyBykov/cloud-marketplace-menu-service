package dev.abykov.cloudmarketplace.menu.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SqlGroup({
        @Sql(
                scripts = "classpath:sql/insert-menu-items.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        ),
        @Sql(
                scripts = "classpath:sql/clear-menu-items.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
})
public abstract class AbstractRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    protected Long getIdByName(String name) {
        return entityManager.createQuery(
                        "select m.id from MenuItem m where m.name = :name",
                        Long.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    protected <T, R> void assertFieldsEquality(T entity, R dto, String... fields) {
        assertThat(entity)
                .usingRecursiveComparison()
                .comparingOnlyFields(fields)
                .isEqualTo(dto);
    }

    protected <T, R> void assertElementsInOrder(
            List<T> items,
            Function<T, R> mapper,
            List<R> expectedElements) {

        List<R> actual = items.stream()
                .map(mapper)
                .toList();

        assertThat(actual).containsExactlyElementsOf(expectedElements);
    }
}
