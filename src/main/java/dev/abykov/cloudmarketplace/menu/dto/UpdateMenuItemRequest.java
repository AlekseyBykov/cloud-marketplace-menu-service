package dev.abykov.cloudmarketplace.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateMenuItemRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private Long preparationTime;
    private Double weight;
    private String imageUrl;
}
