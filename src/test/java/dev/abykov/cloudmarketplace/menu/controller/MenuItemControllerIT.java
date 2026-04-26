package dev.abykov.cloudmarketplace.menu.controller;

import dev.abykov.cloudmarketplace.menu.dto.CreateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemResponse;
import dev.abykov.cloudmarketplace.menu.dto.OrderMenuRequest;
import dev.abykov.cloudmarketplace.menu.dto.OrderMenuResponse;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.entity.MenuItemAttribute;
import dev.abykov.cloudmarketplace.menu.entity.MenuItemAttributes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class MenuItemControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    private MenuItemAttributes validAttributes() {
        return new MenuItemAttributes(List.of(
                new MenuItemAttribute("Milk", 100),
                new MenuItemAttribute("Coffee", 50)
        ));
    }

    @Test
    void createMenuItem_createsItem() {
        var request = CreateMenuItemRequest.builder()
                .name("Latte")
                .description("Coffee")
                .price(BigDecimal.valueOf(5.0))
                .category(Category.DRINKS)
                .preparationTime(120)
                .weight(200)
                .imageUrl("https://images.cloudmarketplace.dev/latte.png")
                .attributes(validAttributes())
                .build();

        var now = LocalDateTime.now();

        webTestClient.post()
                .uri("/api/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MenuItemResponse.class)
                .value(response -> {
                    assertThat(response.getId()).isNotNull();
                    assertThat(response.getName()).isEqualTo(request.getName());
                    assertThat(response.getCreatedAt()).isAfter(now);
                });
    }

    @Test
    void resolveMenuItems_returnsCorrectInfo() {
        var request = OrderMenuRequest.builder()
                .menuNames(List.of("Flat White", "Matcha Latte", "Unknown"))
                .build();

        webTestClient.post()
                .uri("/api/menu-items/resolve")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderMenuResponse.class)
                .value(response -> {

                    var items = response.getItems();

                    assertThat(items).hasSize(3);

                    // 1. Flat White (exists)
                    assertThat(items.get(0).getName()).isEqualTo("Flat White");
                    assertThat(items.get(0).isAvailable()).isTrue();
                    assertThat(items.get(0).getPrice()).isNotNull();

                    // 2. Matcha Latte (exists)
                    assertThat(items.get(1).getName()).isEqualTo("Matcha Latte");
                    assertThat(items.get(1).isAvailable()).isTrue();
                    assertThat(items.get(1).getPrice()).isNotNull();

                    // 3. Unknown (does not exist)
                    assertThat(items.get(2).getName()).isEqualTo("Unknown");
                    assertThat(items.get(2).isAvailable()).isFalse();
                    assertThat(items.get(2).getPrice()).isNull();
                });
    }
}
