package com.fgieracki.restaurantapi.exception;

import com.fgieracki.restaurantapi.payload.ErrorDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ErrorDetails> handleApiException(
            ApiException exception,
            WebRequest webRequest
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                exception.getStatus().value(),
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));
        logger.warn(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, exception.getStatus());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorDetails> handleRuntimeExceptions(
            RuntimeException exception,
            WebRequest webRequest
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));
        logger.warn(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
