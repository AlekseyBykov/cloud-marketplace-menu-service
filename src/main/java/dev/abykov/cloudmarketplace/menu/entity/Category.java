package dev.abykov.cloudmarketplace.menu.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;
import dev.abykov.cloudmarketplace.menu.exception.MenuException;

import java.util.Arrays;

public enum Category {

    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner"),
    DRINKS("drinks"),
    SNACKS("snacks"),
    SALADS("salads");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    /**
     * Используется Jackson при сериализации enum в JSON.
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Позволяет корректно десериализовать строковое значение в enum.
     * Поддерживает различные форматы: "DRINKS", "drinks", "Drinks", "soft-drinks".
     */
    @JsonCreator
    public static Category fromString(String str) {
        if (str == null || str.isBlank()) {
            throw new MenuException(
                    "Category must not be null or blank",
                    HttpStatus.BAD_REQUEST
            );
        }

        String normalized = str.trim()
                .replace("-", "_")
                .toUpperCase();

        return Arrays.stream(Category.values())
                .filter(c -> c.name().equals(normalized)
                        || c.value.equalsIgnoreCase(str))
                .findFirst()
                .orElseThrow(() -> new MenuException(
                        "Failed to create Category from string: %s".formatted(str),
                        HttpStatus.BAD_REQUEST
                ));
    }
}
