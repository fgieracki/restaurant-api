package com.fgieracki.restaurantapi.exception;

import org.springframework.http.HttpStatus;

public class PayloadException extends ApiException {
    public PayloadException(String resourceName, String message) {
        super(HttpStatus.CONFLICT, String.format("%s: %s", resourceName, message));
    }
}
