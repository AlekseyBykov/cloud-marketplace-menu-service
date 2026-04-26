package dev.abykov.cloudmarketplace.menu.service;

import dev.abykov.cloudmarketplace.menu.dto.*;
import dev.abykov.cloudmarketplace.menu.entity.Category;

import java.util.List;

public interface MenuItemService {

    MenuItemResponse create(CreateMenuItemRequest dto);

    void delete(Long id);

    MenuItemResponse update(Long id, UpdateMenuItemRequest dto);

    MenuItemResponse getById(Long id);

    List<MenuItemResponse> getByCategory(Category category, MenuItemSort sortOption);

    OrderMenuResponse resolveMenuItems(OrderMenuRequest request);
}
