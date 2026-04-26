package dev.abykov.cloudmarketplace.menu.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuRequest {

    @NotEmpty(message = "Menu names must not be empty")
    private List<String> menuNames;
}
