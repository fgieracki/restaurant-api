package com.fgieracki.restaurantapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(HttpStatus.NOT_FOUND, String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
    }
}
