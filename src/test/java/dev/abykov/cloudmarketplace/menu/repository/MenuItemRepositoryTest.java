package dev.abykov.cloudmarketplace.menu.repository;

import dev.abykov.cloudmarketplace.menu.dto.MenuItemSort;
import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.entity.MenuItem;
import dev.abykov.cloudmarketplace.menu.repository.updater.MenuItemFieldUpdaterConfig;
import dev.abykov.cloudmarketplace.menu.util.TestDataFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(MenuItemFieldUpdaterConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MenuItemRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private MenuItemRepository repository;

    @Disabled
    @Test
    void updateMenuItem_updatesAllFields_whenFullRequestProvided() {
        UpdateMenuItemRequest request = TestDataFactory.fullUpdateRequest();
        Long id = getIdByName("Flat White");

        int updateCount = repository.updateMenuItem(id, request);

        assertThat(updateCount).isEqualTo(1);

        MenuItem updated = repository.findById(id).orElseThrow();
        assertFieldsEquality(updated, request,
                "name", "description", "price",
                "preparationTime", "imageUrl", "weight");
    }

    @Test
    void updateMenuItem_updatesOnlyProvidedFields_whenPartialRequestProvided() {
        var request = UpdateMenuItemRequest.builder()
                .price(BigDecimal.valueOf(18.99))
                .description("Updated description")
                .imageUrl("https://images.cloudmarketplace.dev/updated-flat-white.png")
                .build();

        Long id = getIdByName("Flat White");

        int updateCount = repository.updateMenuItem(id, request);

        assertThat(updateCount).isEqualTo(1);

        MenuItem updated = repository.findById(id).orElseThrow();
        assertFieldsEquality(updated, request,
                "price", "description", "imageUrl");
    }

    @Disabled
    @Test
    void updateMenuItem_throwsException_whenNameIsNotUnique() {
        var request = UpdateMenuItemRequest.builder()
                .name("Berry Smoothie") // уже существует
                .build();

        Long id = getIdByName("Flat White");

        assertThatThrownBy(() -> repository.updateMenuItem(id, request))
                .isInstanceOfAny(DataIntegrityViolationException.class, JpaSystemException.class);
    }

    @Test
    void updateMenuItem_returnsZero_whenMenuItemDoesNotExist() {
        UpdateMenuItemRequest request = TestDataFactory.fullUpdateRequest();

        int updateCount = repository.updateMenuItem(9999L, request);

        assertThat(updateCount).isEqualTo(0);
    }

    @Test
    void findAllByCategorySorted_returnsDrinks_sortedByPriceAsc() {
        List<MenuItem> drinks = repository
                .findAllByCategorySorted(Category.DRINKS, MenuItemSort.PRICE_ASC);

        assertThat(drinks).hasSize(3);
        assertElementsInOrder(drinks, MenuItem::getName,
                List.of("Flat White", "Matcha Latte", "Berry Smoothie"));
    }

    @Test
    void findAllByCategorySorted_returnsDrinks_sortedByPriceDesc() {
        List<MenuItem> drinks = repository
                .findAllByCategorySorted(Category.DRINKS, MenuItemSort.PRICE_DESC);

        assertThat(drinks).hasSize(3);
        assertElementsInOrder(drinks, MenuItem::getName,
                List.of("Berry Smoothie", "Matcha Latte", "Flat White"));
    }

    @Test
    void findAllByCategorySorted_returnsDrinks_sortedByNameAsc() {
        List<MenuItem> drinks = repository
                .findAllByCategorySorted(Category.DRINKS, MenuItemSort.NAME_ASC);

        assertThat(drinks).hasSize(3);
        assertElementsInOrder(drinks, MenuItem::getName,
                List.of("Berry Smoothie", "Flat White", "Matcha Latte"));
    }

    @Test
    void findAllByCategorySorted_returnsDrinks_sortedByNameDesc() {
        List<MenuItem> drinks = repository
                .findAllByCategorySorted(Category.DRINKS, MenuItemSort.NAME_DESC);

        assertThat(drinks).hasSize(3);
        assertElementsInOrder(drinks, MenuItem::getName,
                List.of("Matcha Latte", "Flat White", "Berry Smoothie"));
    }
}
