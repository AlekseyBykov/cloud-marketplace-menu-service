package dev.abykov.cloudmarketplace.menu.repository;

import dev.abykov.cloudmarketplace.menu.entity.MenuItem;
import dev.abykov.cloudmarketplace.menu.repository.projection.MenuItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MenuItemRepository
        extends JpaRepository<MenuItem, Long>, CustomMenuItemRepository {

    @Query("""
                select new dev.abykov.cloudmarketplace.menu.repository.projection.MenuItemProjection(
                    m.name,
                    m.price
                )
                from MenuItem m
                where m.name in :names
            """)
    List<MenuItemProjection> findByNameIn(@Param("names") Set<String> names);
}
