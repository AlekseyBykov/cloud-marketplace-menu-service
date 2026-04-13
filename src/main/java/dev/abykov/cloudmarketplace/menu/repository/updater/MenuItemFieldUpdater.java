package dev.abykov.cloudmarketplace.menu.repository.updater;

import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.MenuItem;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class MenuItemFieldUpdater<V> {

    private final SingularAttribute<MenuItem, V> attribute;
    private final Function<UpdateMenuItemRequest, V> valueExtractor;

    public void apply(CriteriaUpdate<MenuItem> update, UpdateMenuItemRequest request) {
        V value = valueExtractor.apply(request);
        if (value != null) {
            update.set(attribute, value);
        }
    }
}
