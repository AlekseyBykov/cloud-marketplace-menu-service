package dev.abykov.cloudmarketplace.menu.exception;

/**
 * Thrown when an unsupported menu item sort option is requested.
 */
public class InvalidMenuItemSortException extends RuntimeException {

    private InvalidMenuItemSortException(String message) {
        super(message);
    }

    public static InvalidMenuItemSortException unsupported(String value) {
        return new InvalidMenuItemSortException(
                "Unsupported sort option: '%s'.".formatted(value)
        );
    }

    public static InvalidMenuItemSortException emptyValue() {
        return new InvalidMenuItemSortException(
                "Sort option must not be blank."
        );
    }
}
