package com.anywayclear.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final ExceptionCode exceptionCode;
    private final HttpStatus httpStatus;
    private final String message;

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode, exceptionCode.getHttpStatus(), exceptionCode.getMessage());
    }
}
