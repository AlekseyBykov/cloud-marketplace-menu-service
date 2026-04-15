package dev.abykov.cloudmarketplace.menu.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
public abstract class AbstractServiceIntegrationTest {

    @Autowired
    protected EntityManager entityManager;

    protected Long getIdByName(String name) {
        return entityManager.createQuery(
                        "select m.id from MenuItem m where m.name = :name", Long.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    protected <T, R> void assertFieldsEquality(T actual, R expected, String... fields) {
        assertFieldsExistence(actual, expected, fields);

        assertThat(actual)
                .usingRecursiveComparison()
                .withComparatorForType(
                        (BigDecimal bd1, BigDecimal bd2) -> bd1.compareTo(bd2),
                        BigDecimal.class
                )
                .comparingOnlyFields(fields)
                .isEqualTo(expected);
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

    private <T, R> void assertFieldsExistence(T item, R dto, String... fields) {
        boolean itemFieldsMissing = Arrays.stream(fields)
                .anyMatch(field -> getField(item, field) == null);

        boolean dtoFieldsMissing = Arrays.stream(fields)
                .anyMatch(field -> getField(dto, field) == null);

        if (itemFieldsMissing || dtoFieldsMissing) {
            throw new AssertionError(
                    "One or more fields do not exist. Actual: %s, Expected: %s, Fields: %s"
                            .formatted(item, dto, List.of(fields)));
        }
    }

    private <T> Field getField(T item, String fieldName) {
        try {
            return item.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
