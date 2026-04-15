package dev.abykov.cloudmarketplace.menu.service;

import dev.abykov.cloudmarketplace.menu.dto.CreateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemResponse;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemSort;
import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.exception.DuplicateMenuItemException;
import dev.abykov.cloudmarketplace.menu.exception.MenuItemNotFoundException;
import dev.abykov.cloudmarketplace.menu.repository.MenuItemRepository;
import dev.abykov.cloudmarketplace.menu.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuItemServiceTest extends AbstractServiceIntegrationTest {

    @Autowired
    private MenuItemService service;
    @Autowired
    private MenuItemRepository repository;

    @Test
    void getById_returnsMenuItem_whenItemExists() {
        Long id = getIdByName("Flat White");

        MenuItemResponse response = service.getById(id);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo("Flat White");
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    void getById_throwsException_whenItemDoesNotExist() {
        assertThrows(
                MenuItemNotFoundException.class,
                () -> service.getById(9999L)
        );
    }

    @Test
    void getByCategory_returnsSortedList() {
        List<MenuItemResponse> drinks =
                service.getByCategory(Category.DRINKS, MenuItemSort.NAME_ASC);

        assertThat(drinks).hasSize(3);
        assertElementsInOrder(
                drinks,
                MenuItemResponse::getName,
                List.of("Berry Smoothie", "Flat White", "Matcha Latte")
        );
    }

    @Test
    void delete_removesMenuItem() {
        Long id = getIdByName("Flat White");

        service.delete(id);

        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void create_createsMenuItemSuccessfully() {
        CreateMenuItemRequest request = CreateMenuItemRequest.builder()
                .name("New Latte")
                .description("Freshly brewed latte")
                .price(java.math.BigDecimal.valueOf(10.50))
                .category(Category.DRINKS)
                .preparationTime(120)
                .weight(250)
                .imageUrl("https://images.cloudmarketplace.dev/new-latte.png")
                .attributes(TestDataFactory.defaultAttributes())
                .build();

        LocalDateTime now = LocalDateTime.now().minusSeconds(1);

        MenuItemResponse result = service.create(request);

        assertThat(result.getId()).isNotNull();
        assertFieldsEquality(result, request,
                "name", "description", "price", "imageUrl");
        assertThat(result.getCreatedAt()).isAfter(now);
        assertThat(result.getUpdatedAt()).isAfter(now);
    }

    @Test
    void create_throwsException_whenNameIsNotUnique() {
        CreateMenuItemRequest request = CreateMenuItemRequest.builder()
                .name("Flat White") // already exists
                .description("Duplicate")
                .price(java.math.BigDecimal.TEN)
                .category(Category.DRINKS)
                .preparationTime(100)
                .weight(200)
                .imageUrl("https://images.cloudmarketplace.dev/duplicate.png")
                .attributes(TestDataFactory.defaultAttributes())
                .build();

        assertThrows(
                DuplicateMenuItemException.class,
                () -> service.create(request)
        );
    }

    @Test
    void update_updatesMenuItemSuccessfully() {
        Long id = getIdByName("Flat White");
        UpdateMenuItemRequest update = TestDataFactory.fullUpdateRequest();

        MenuItemResponse updated = service.update(id, update);

        assertFieldsEquality(updated, update,
                "name", "description", "price",
                "preparationTime", "imageUrl", "weight");
    }

    @Test
    void update_throwsException_whenItemDoesNotExist() {
        UpdateMenuItemRequest update = TestDataFactory.fullUpdateRequest();

        assertThrows(
                MenuItemNotFoundException.class,
                () -> service.update(9999L, update)
        );
    }

    @Test
    void update_throwsException_whenNameIsNotUnique() {
        Long id = getIdByName("Flat White");

        UpdateMenuItemRequest update = UpdateMenuItemRequest.builder()
                .name("Berry Smoothie") // already exists
                .build();

        assertThrows(
                DuplicateMenuItemException.class,
                () -> service.update(id, update)
        );
    }
}
