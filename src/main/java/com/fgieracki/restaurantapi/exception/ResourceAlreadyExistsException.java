package com.fgieracki.restaurantapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.CONFLICT)

public class ResourceAlreadyExistsException extends ApiException {
    public ResourceAlreadyExistsException(String resourceName, String fieldName, String fieldValue) {
        super(HttpStatus.CONFLICT, String.format("%s already exists with %s: %s", resourceName, fieldName, fieldValue));
    }

    public ResourceAlreadyExistsException(String resourceName, String fieldName1, String fieldValue1,
                                          String fieldName2, String fieldValue2) {
        super(HttpStatus.CONFLICT, String.format("%s already exists with %s: %s and %s: %s",
                resourceName, fieldName1, fieldValue1, fieldName2, fieldValue2));
    }
}
