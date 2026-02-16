package com.gustcustodio.weather_api.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleClientError(HttpClientErrorException e) {
        String message = switch (e.getStatusCode().value()) {
            case 400 -> "Bad Request: The format of the API is incorrect or an invalid parameter was supplied.";
            case 401 -> "Unauthorized: Problem with the API key, account, or subscription.";
            case 404 -> "Not Found: The request cannot be matched to any valid API endpoint.";
            case 429 -> "Too Many Requests: The account has exceeded its assigned limits.";
            default -> "External API Error: " + e.getStatusText();
        };

        return createResponse(message, (HttpStatus) e.getStatusCode());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleServerError(HttpServerErrorException e) {
        return createResponse("Internal Server Error: The weather service is currently experiencing issues.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralError(Exception e) {
        return createResponse("An unexpected error occurred: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> createResponse(String message, HttpStatus status) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
        return new ResponseEntity<>(body, status);
    }

}
