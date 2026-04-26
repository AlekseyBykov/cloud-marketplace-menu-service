package dev.abykov.cloudmarketplace.menu.repository.projection;

import java.math.BigDecimal;

public record MenuItemProjection(
        String name,
        BigDecimal price
) {
}
