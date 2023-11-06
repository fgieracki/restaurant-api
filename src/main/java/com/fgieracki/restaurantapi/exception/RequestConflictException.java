package com.fgieracki.restaurantapi.exception;

import org.springframework.http.HttpStatus;

public class RequestConflictException extends ApiException {
    public RequestConflictException(String resourceName, String fieldName, String fieldPathValue, String fieldBodyValue) {
        super(HttpStatus.CONFLICT, String.format("%s: %s field value from path, conflicts with request body value %s: %s",
                resourceName, fieldName, fieldPathValue, fieldBodyValue));
    }
}