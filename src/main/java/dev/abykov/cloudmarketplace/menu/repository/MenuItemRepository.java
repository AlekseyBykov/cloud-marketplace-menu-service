package dev.abykov.cloudmarketplace.menu.repository;

import dev.abykov.cloudmarketplace.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository
        extends JpaRepository<MenuItem, Long>, CustomMenuItemRepository {
}
