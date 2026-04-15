package dev.abykov.cloudmarketplace.menu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.entity.MenuItemAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItemResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private long preparationTime;
    private double weight;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MenuItemAttributes attributes;
}
