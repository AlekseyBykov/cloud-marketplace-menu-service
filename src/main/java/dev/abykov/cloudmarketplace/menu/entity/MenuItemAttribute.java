package dev.abykov.cloudmarketplace.menu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemAttribute {

    private String name;
    private int calories;
}
