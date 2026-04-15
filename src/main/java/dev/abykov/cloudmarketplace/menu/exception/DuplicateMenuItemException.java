package dev.abykov.cloudmarketplace.menu.exception;

public class DuplicateMenuItemException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE =
            "Menu item with name '%s' already exists.";

    private DuplicateMenuItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DuplicateMenuItemException withName(String name, Throwable cause) {
        return new DuplicateMenuItemException(
                MESSAGE_TEMPLATE.formatted(name),
                cause
        );
    }
}
