package dev.abykov.cloudmarketplace.menu.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MenuException extends RuntimeException {

    private final HttpStatus status;

    public MenuException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
