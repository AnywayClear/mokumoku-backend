package com.anywayclear.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.anywayclear.exception.ExceptionCode.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.client.HttpClientErrorException.Forbidden;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 비즈니스 로직 에러
     */
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorResponse handleCustomException(CustomException customException) {
        return ErrorResponse.of(customException.getExceptionCode());
    }

    /**
     * 자원에 대한 권한 업음
     */
    @ExceptionHandler(Forbidden.class)
    @ResponseStatus(FORBIDDEN)
    private ErrorResponse handleForbiddenException() {
        return ErrorResponse.of(INVALID_AUTH);
    }

    /**
     * 요청한 URI에 대한 리소스 없음
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    private ErrorResponse handleResourceException() {
        return ErrorResponse.of(INVALID_RESOURCE);
    }

    /**
     * 그 외 에러
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    private ErrorResponse handleException(Exception e) {
        return new ErrorResponse(INTERNAL_SERVER_ERROR,e.getMessage());
    }
}
