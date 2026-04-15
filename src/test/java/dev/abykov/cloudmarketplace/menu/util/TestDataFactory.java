package dev.abykov.cloudmarketplace.menu.util;

import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.MenuItemAttribute;
import dev.abykov.cloudmarketplace.menu.entity.MenuItemAttributes;

import java.math.BigDecimal;
import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static UpdateMenuItemRequest fullUpdateRequest() {
        return UpdateMenuItemRequest.builder()
                .name("Updated Flat White")
                .description("Updated description")
                .price(BigDecimal.valueOf(21.50))
                .preparationTime(300L)
                .weight(250.0)
                .imageUrl("https://images.cloudmarketplace.dev/updated-flat-white.png")
                .build();
    }

    public static MenuItemAttributes defaultAttributes() {
        return new MenuItemAttributes(
                List.of(
                        new MenuItemAttribute("espresso", 5),
                        new MenuItemAttribute("milk", 60)
                )
        );
    }
}
