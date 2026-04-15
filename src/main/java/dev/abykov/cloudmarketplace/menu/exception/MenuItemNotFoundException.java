package dev.abykov.cloudmarketplace.menu.exception;

public class MenuItemNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE =
            "Menu item with id=%d was not found.";

    private MenuItemNotFoundException(String message) {
        super(message);
    }

    public static MenuItemNotFoundException byId(Long id) {
        return new MenuItemNotFoundException(
                MESSAGE_TEMPLATE.formatted(id)
        );
    }
}
