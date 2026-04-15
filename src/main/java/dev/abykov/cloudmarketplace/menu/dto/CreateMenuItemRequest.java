package dev.abykov.cloudmarketplace.menu.dto;

import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.entity.MenuItemAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Request payload for creating a new menu item")
public class CreateMenuItemRequest {

    @NotBlank(message = "Menu item name must not be blank")
    @Size(max = 255, message = "Menu item name must not exceed 255 characters")
    @Schema(
            description = "Unique name of the menu item",
            example = "Flat White"
    )
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Schema(
            description = "Detailed description of the menu item",
            example = "Espresso with velvety microfoam milk"
    )
    private String description;

    @NotNull(message = "Price must be provided")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 integer digits and 2 decimal places")
    @Schema(
            description = "Price of the menu item",
            example = "12.50"
    )
    private BigDecimal price;

    @NotNull(message = "Category must be specified")
    @Schema(
            description = "Category of the menu item",
            example = "DRINKS"
    )
    private Category category;

    @Positive(message = "Preparation time must be greater than 0 seconds")
    @Schema(
            description = "Preparation time in seconds",
            example = "180"
    )
    private long preparationTime;

    @Positive(message = "Weight must be greater than 0 grams")
    @Schema(
            description = "Weight of the menu item in grams",
            example = "220"
    )
    private double weight;

    @NotBlank(message = "Image URL must not be blank")
    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Image URL must be a valid HTTP or HTTPS URL"
    )
    @Schema(
            description = "URL of the menu item image",
            example = "https://images.cloudmarketplace.dev/flat-white.png"
    )
    private String imageUrl;

    @NotNull(message = "Attributes must be provided")
    @Schema(
            description = "Nutritional and ingredient attributes of the menu item"
    )
    private MenuItemAttributes attributes;
}
