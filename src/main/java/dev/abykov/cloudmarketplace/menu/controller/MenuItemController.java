package dev.abykov.cloudmarketplace.menu.controller;

import dev.abykov.cloudmarketplace.menu.dto.CreateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemResponse;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemSort;
import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.entity.Category;
import dev.abykov.cloudmarketplace.menu.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing marketplace menu items.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu-items")
@Tag(name = "Menu Items", description = "REST API for managing menu items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Operation(
            summary = "Create a new menu item",
            description = "Creates a new menu item and returns the persisted representation"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Menu item created successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Menu item with the same name already exists",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse create(
            @RequestBody @Valid CreateMenuItemRequest request
    ) {
        log.info("Received request to create menu item: {}", request);
        return menuItemService.create(request);
    }

    @Operation(
            summary = "Delete a menu item",
            description = "Deletes a menu item by its identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Menu item deleted successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Menu item was not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @Positive(message = "Menu item id must be greater than 0") Long id
    ) {
        log.info("Received request to delete menu item with id={}", id);
        menuItemService.delete(id);
    }

    @Operation(
            summary = "Update a menu item",
            description = "Partially updates an existing menu item"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Menu item was not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Menu item with the same name already exists",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PatchMapping("/{id}")
    public MenuItemResponse update(
            @PathVariable @Positive(message = "Menu item id must be greater than 0") Long id,
            @RequestBody @Valid UpdateMenuItemRequest request
    ) {
        log.info("Received request to update menu item with id={}, payload={}", id, request);
        return menuItemService.update(id, request);
    }

    @Operation(
            summary = "Get a menu item by id",
            description = "Returns a single menu item by its identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Menu item was not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{id}")
    public MenuItemResponse getById(
            @PathVariable @Positive(message = "Menu item id must be greater than 0") Long id
    ) {
        log.info("Received request to fetch menu item with id={}", id);
        return menuItemService.getById(id);
    }

    @Operation(
            summary = "Get menu items by category",
            description = "Returns menu items for a category sorted by the requested sort option"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category or sort parameter",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping
    public List<MenuItemResponse> getByCategory(
            @RequestParam @NotBlank(message = "Category must not be blank") String category,
            @RequestParam(defaultValue = "name_asc")
            @NotBlank(message = "Sort option must not be blank") String sort
    ) {
        log.info("Received request to fetch menu items for category={}, sort={}", category, sort);
        return menuItemService.getByCategory(
                Category.fromString(category),
                MenuItemSort.fromString(sort)
        );
    }
}
