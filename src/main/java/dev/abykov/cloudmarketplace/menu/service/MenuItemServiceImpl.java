package dev.abykov.cloudmarketplace.menu.service;

import dev.abykov.cloudmarketplace.menu.dto.*;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.entity.MenuItem;
import dev.abykov.cloudmarketplace.menu.exception.DuplicateMenuItemException;
import dev.abykov.cloudmarketplace.menu.exception.MenuItemNotFoundException;
import dev.abykov.cloudmarketplace.menu.mapper.MenuItemMapper;
import dev.abykov.cloudmarketplace.menu.repository.MenuItemRepository;
import dev.abykov.cloudmarketplace.menu.repository.projection.MenuItemProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository repository;
    private final MenuItemMapper mapper;

    @Override
    @Transactional
    public MenuItemResponse create(CreateMenuItemRequest dto) {
        try {
            MenuItem entity = mapper.toDomain(dto);
            return mapper.toDto(repository.save(entity));
        } catch (DataIntegrityViolationException ex) {
            throw DuplicateMenuItemException.withName(dto.getName(), ex);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MenuItem entity = getMenuItemOrThrow(id);
        repository.delete(entity);
    }

    @Override
    @Transactional
    public MenuItemResponse update(Long id, UpdateMenuItemRequest dto) {
        try {
            int updated = repository.updateMenuItem(id, dto);
            if (updated == 0) {
                throw MenuItemNotFoundException.byId(id);
            }
            MenuItem updatedEntity = getMenuItemOrThrow(id);
            return mapper.toDto(updatedEntity);
        } catch (DataIntegrityViolationException ex) {
            throw DuplicateMenuItemException.withName(dto.getName(), ex);
        }
    }

    @Override
    public MenuItemResponse getById(Long id) {
        return mapper.toDto(getMenuItemOrThrow(id));
    }

    @Override
    public List<MenuItemResponse> getByCategory(Category category, MenuItemSort sortOption) {
        return mapper.toResponseList(
                repository.findAllByCategorySorted(category, sortOption)
        );
    }

    @Override
    public OrderMenuResponse resolveMenuItems(OrderMenuRequest request) {
        Map<String, MenuItemProjection> menuMap =
                repository.findByNameIn(Set.copyOf(request.getMenuNames()))
                        .stream()
                        .collect(Collectors.toMap(
                                MenuItemProjection::name,
                                Function.identity()
                        ));

        List<MenuInfo> items = request.getMenuNames().stream()
                .map(name -> toMenuInfo(name, menuMap.get(name)))
                .toList();

        return OrderMenuResponse.builder()
                .items(items)
                .build();
    }

    private MenuItem getMenuItemOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> MenuItemNotFoundException.byId(id));
    }

    private MenuInfo toMenuInfo(String name, MenuItemProjection projection) {
        if (projection == null) {
            return new MenuInfo(name, null, false);
        }

        return new MenuInfo(
                projection.name(),
                projection.price(),
                true
        );
    }
}
