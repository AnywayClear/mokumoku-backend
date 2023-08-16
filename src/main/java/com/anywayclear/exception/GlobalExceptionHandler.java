package com.anywayclear.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

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
     * 로그인이 필요한 경우 임시 추가
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(UNAUTHORIZED)
    private ErrorResponse handleUnAuthorizedException() {
        return new ErrorResponse(INVALID_TOKEN, UNAUTHORIZED, "로그인후 진행해주세요.");
    }

    /**
     * 자원에 대한 권한 없음
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

//    /**
//     * 그 외 에러
//     */
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(INTERNAL_SERVER_ERROR)
//    private ErrorResponse handleException(Exception e) {
//        return new ErrorResponse(INTERNAL_SERVER_ERROR,e.getMessage());
//    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    private ErrorResponse handleConstraintViolationException() {
        return ErrorResponse.of(INVALID_INSERT_REQUEST);
    }
}
