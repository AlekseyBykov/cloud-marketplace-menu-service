package dev.abykov.cloudmarketplace.menu.controller;

import dev.abykov.cloudmarketplace.menu.dto.CreateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemResponse;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.entity.MenuItemAttribute;
import dev.abykov.cloudmarketplace.menu.entity.MenuItemAttributes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
}
