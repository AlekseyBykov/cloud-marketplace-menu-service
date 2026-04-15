package dev.abykov.cloudmarketplace.menu.service;

import dev.abykov.cloudmarketplace.menu.dto.CreateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemResponse;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemSort;
import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.Category;

import java.util.List;

public interface MenuItemService {

    MenuItemResponse create(CreateMenuItemRequest dto);

    void delete(Long id);

    MenuItemResponse update(Long id, UpdateMenuItemRequest dto);

    MenuItemResponse getById(Long id);

    List<MenuItemResponse> getByCategory(Category category, MenuItemSort sortOption);
}
