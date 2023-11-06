package com.fgieracki.restaurantapi.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorDetails {
    private int status;
    private Date timestamp;
    private String message;
    private String details;

    @Override
    public String toString() {
         return "{" +
                 "\"status\":" + status +
                 ", \"timestamp\":" + timestamp +
                 ", \"message\":\"" + message + "\"" +
                 ", \"details\":\"" + details + "\"" +
                 "}";
    }
}
