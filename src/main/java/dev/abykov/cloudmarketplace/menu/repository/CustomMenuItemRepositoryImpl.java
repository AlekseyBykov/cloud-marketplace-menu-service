package dev.abykov.cloudmarketplace.menu.repository;

import dev.abykov.cloudmarketplace.menu.dto.MenuItemSort;
import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.entity.MenuItem;
import dev.abykov.cloudmarketplace.menu.entity.MenuItem_;
import dev.abykov.cloudmarketplace.menu.repository.updater.MenuItemFieldUpdater;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CustomMenuItemRepositoryImpl implements CustomMenuItemRepository {

    private final EntityManager entityManager;
    private final List<MenuItemFieldUpdater<?>> fieldUpdaters;

    public CustomMenuItemRepositoryImpl(
            EntityManager entityManager,
            List<MenuItemFieldUpdater<?>> fieldUpdaters
    ) {
        this.entityManager = entityManager;
        this.fieldUpdaters = fieldUpdaters;
    }

    @Override
    @Transactional
    public int updateMenuItem(
            Long id,
            UpdateMenuItemRequest request
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<MenuItem> updateQuery = cb.createCriteriaUpdate(MenuItem.class);
        Root<MenuItem> root = updateQuery.from(MenuItem.class);

        fieldUpdaters.forEach(updater -> updater.apply(updateQuery, request));

        updateQuery.where(cb.equal(root.get(MenuItem_.id), id));

        int updatedCount = entityManager.createQuery(updateQuery).executeUpdate();

        // Синхронизация persistence context
        entityManager.flush();
        entityManager.clear();

        return updatedCount;
    }

    @Override
    public List<MenuItem> findAllByCategorySorted(
            Category category,
            MenuItemSort sort
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MenuItem> selectQuery = cb.createQuery(MenuItem.class);
        Root<MenuItem> root = selectQuery.from(MenuItem.class);

        selectQuery.select(root)
                .where(cb.equal(root.get(MenuItem_.category), category))
                .orderBy(sort.toOrder(cb, root));

        TypedQuery<MenuItem> typedQuery = entityManager.createQuery(selectQuery);
        return typedQuery.getResultList();
    }
}
