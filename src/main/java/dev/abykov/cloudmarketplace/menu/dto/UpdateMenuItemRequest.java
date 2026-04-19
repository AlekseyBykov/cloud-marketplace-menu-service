package dev.abykov.cloudmarketplace.menu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.abykov.cloudmarketplace.menu.validation.NullableNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request payload for partial menu item update")
public class UpdateMenuItemRequest {

    @NullableNotBlank(message = "Menu item name must not be blank")
    @Schema(description = "Updated menu item name", example = "Updated Flat White")
    private String name;

    @NullableNotBlank(message = "Description must not be blank")
    @Schema(description = "Updated description", example = "Updated description")
    private String description;

    @Positive(message = "Price must be greater than 0")
    @Schema(description = "Updated price", example = "21.50")
    private BigDecimal price;

    @Positive(message = "Preparation time must be greater than 0 seconds")
    @Schema(description = "Updated preparation time in seconds", example = "300")
    private Long preparationTime;

    @Positive(message = "Weight must be greater than 0 grams")
    @Schema(description = "Updated weight in grams", example = "250")
    private Double weight;

    @NullableNotBlank(message = "Image URL must not be blank")
    @Schema(description = "Updated image URL", example = "https://images.cloudmarketplace.dev/updated-flat-white.png")
    private String imageUrl;
}
