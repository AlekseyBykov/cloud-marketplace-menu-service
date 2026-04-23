package dev.abykov.cloudmarketplace.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abykov.cloudmarketplace.menu.dto.MenuItemResponse;
import dev.abykov.cloudmarketplace.menu.dto.UpdateMenuItemRequest;
import dev.abykov.cloudmarketplace.menu.exception.ApiExceptionHandler;
import dev.abykov.cloudmarketplace.menu.exception.MenuItemNotFoundException;
import dev.abykov.cloudmarketplace.menu.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuItemController.class)
@Import(ApiExceptionHandler.class)
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getById_returnsMenuItem_whenExists() throws Exception {
        var response = MenuItemResponse.builder()
                .id(1L)
                .name("Cappuccino")
                .description("Coffee")
                .price(BigDecimal.valueOf(5.0))
                .category(null)
                .preparationTime(120)
                .weight(200)
                .imageUrl("img")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .attributes(null)
                .build();

        when(service.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/menu-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cappuccino"))
                .andExpect(jsonPath("$.price").value(5.0));
    }

    @Test
    void getById_returnsNotFound_whenItemDoesNotExist() throws Exception {
        long id = 1L;
        var ex = MenuItemNotFoundException.byId(id);

        when(service.getById(id)).thenThrow(ex);

        mockMvc.perform(get("/api/menu-items/{id}", id))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.detail").value(ex.getMessage())
                );
    }

    @Test
    void create_returnsBadRequest_whenInvalidPayload() throws Exception {
        var invalidJson = """
                {
                    "name": "",
                    "price": -10
                }
                """;

        mockMvc.perform(post("/api/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalid_params").exists());
    }

    @Test
    void getByCategory_returnsList_whenValidParams() throws Exception {
        var items = List.of(
                MenuItemResponse.builder().id(1L).name("Cappuccino").build(),
                MenuItemResponse.builder().id(2L).name("Tea").build()
        );

        when(service.getByCategory(any(), any())).thenReturn(items);

        mockMvc.perform(get("/api/menu-items")
                        .param("category", "drinks")
                        .param("sort", "name_asc"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(2),
                        jsonPath("$[0].name").value("Cappuccino"),
                        jsonPath("$[1].name").value("Tea")
                );
    }

    @Test
    void getByCategory_returnsEmptyList_whenNoItems() throws Exception {
        when(service.getByCategory(any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/menu-items")
                        .param("category", "drinks")
                        .param("sort", "name_asc"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(0)
                );
    }

    @Test
    void delete_returnsNotFound_whenItemDoesNotExist() throws Exception {
        long id = 1L;
        var ex = MenuItemNotFoundException.byId(id);

        doThrow(ex).when(service).delete(id);

        mockMvc.perform(delete("/api/menu-items/{id}", id))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.detail").value(ex.getMessage())
                );
    }

    @Test
    void update_returnsUpdatedItem_whenValidRequest() throws Exception {
        long id = 1L;

        var request = UpdateMenuItemRequest.builder()
                .name("Latte")
                .price(BigDecimal.valueOf(6.0))
                .build();

        var response = MenuItemResponse.builder()
                .id(id)
                .name("Latte")
                .price(BigDecimal.valueOf(6.0))
                .build();

        when(service.update(eq(id), any())).thenReturn(response);

        mockMvc.perform(patch("/api/menu-items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.name").value("Latte"),
                        jsonPath("$.price").value(6.0)
                );
    }

    @Test
    void update_returnsNotFound_whenItemDoesNotExist() throws Exception {
        long id = 1L;

        var request = UpdateMenuItemRequest.builder()
                .name("Latte")
                .build();

        var ex = MenuItemNotFoundException.byId(id);

        when(service.update(eq(id), any())).thenThrow(ex);

        mockMvc.perform(patch("/api/menu-items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.detail").value(ex.getMessage())
                );
    }
}
