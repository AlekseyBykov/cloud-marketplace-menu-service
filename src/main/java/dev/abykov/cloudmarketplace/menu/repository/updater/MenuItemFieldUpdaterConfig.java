package dev.abykov.cloudmarketplace.menu.repository.updater;

import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.MenuItem_;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class MenuItemFieldUpdaterConfig {

    @Bean
    MenuItemFieldUpdater<String> nameUpdater() {
        return new MenuItemFieldUpdater<>(MenuItem_.name, UpdateMenuItemRequest::getName);
    }

    @Bean
    MenuItemFieldUpdater<String> descriptionUpdater() {
        return new MenuItemFieldUpdater<>(MenuItem_.description, UpdateMenuItemRequest::getDescription);
    }

    @Bean
    MenuItemFieldUpdater<BigDecimal> priceUpdater() {
        return new MenuItemFieldUpdater<>(MenuItem_.price, UpdateMenuItemRequest::getPrice);
    }

    @Bean
    MenuItemFieldUpdater<String> imageUrlUpdater() {
        return new MenuItemFieldUpdater<>(MenuItem_.imageUrl, UpdateMenuItemRequest::getImageUrl);
    }

    @Bean
    MenuItemFieldUpdater<Long> preparationTimeUpdater() {
        return new MenuItemFieldUpdater<>(MenuItem_.preparationTime, UpdateMenuItemRequest::getPreparationTime);
    }

    @Bean
    MenuItemFieldUpdater<Double> weightUpdater() {
        return new MenuItemFieldUpdater<>(MenuItem_.weight, UpdateMenuItemRequest::getWeight);
    }
}
