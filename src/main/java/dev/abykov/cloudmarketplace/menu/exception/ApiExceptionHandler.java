package dev.abykov.cloudmarketplace.menu.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized REST exception handling for the API layer.
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MenuItemNotFoundException.class)
    public ProblemDetail handleNotFound(MenuItemNotFoundException ex, WebRequest request) {
        log.warn("Menu item not found: {}", ex.getMessage());
        return createProblemDetail(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DuplicateMenuItemException.class)
    public ProblemDetail handleConflict(DuplicateMenuItemException ex, WebRequest request) {
        log.warn("Duplicate menu item detected: {}", ex.getMessage());
        return createProblemDetail(ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return createProblemDetail(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(InvalidMenuItemSortException.class)
    public ProblemDetail handleInvalidSort(InvalidMenuItemSortException ex, WebRequest request) {
        log.warn("Invalid sort option: {}", ex.getMessage());
        return createProblemDetail(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.warn("Request body could not be parsed: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(createProblemDetail("Request body is malformed or unreadable", HttpStatus.BAD_REQUEST, request));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ProblemDetail problemDetail = ex.getBody();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ex.getBindingResult().getGlobalErrors().forEach(error ->
                errors.put(error.getObjectName(), error.getDefaultMessage()));

        log.warn("Request validation failed: {}", errors);

        problemDetail.setStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("invalid_params", errors);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ProblemDetail problemDetail = ex.getBody();
        Map<String, String> errors = new HashMap<>();

        ex.getAllValidationResults().forEach(result ->
                result.getResolvableErrors().forEach(error ->
                        errors.put(result.getMethodParameter().getParameterName(), error.getDefaultMessage()))
        );

        log.warn("Handler method validation failed: {}", errors);

        problemDetail.setStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("invalid_params", errors);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
    }

    private static ProblemDetail createProblemDetail(
            String message,
            HttpStatus status,
            WebRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));
        return problemDetail;
    }
}
