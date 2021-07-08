package com.example.dynamoDemo.domain.exceptions;

import static java.lang.String.format;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

    public String errorText;

    public ErrorResponse(String format, Object... args) {
        this(format(format, args));
    }
}
