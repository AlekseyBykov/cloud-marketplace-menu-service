package dev.abykov.cloudmarketplace.menu.repository;

import dev.abykov.cloudmarketplace.menu.dto.MenuItemSort;
import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.entity.MenuItem;

import java.util.List;

public interface CustomMenuItemRepository {

    int updateMenuItem(Long id, UpdateMenuItemRequest request);

    List<MenuItem> findAllByCategorySorted(Category category, MenuItemSort sort);
}
