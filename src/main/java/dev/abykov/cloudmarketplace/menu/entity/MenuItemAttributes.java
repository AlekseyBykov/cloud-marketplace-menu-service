package dev.abykov.cloudmarketplace.menu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemAttributes {

    private List<MenuItemAttribute> attributes;
}
