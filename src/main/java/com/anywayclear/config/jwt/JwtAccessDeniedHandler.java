package com.anywayclear.config.jwt;

import com.anywayclear.exception.ErrorResponse;
import com.anywayclear.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        sendJsonResponse(response, ExceptionCode.INVALID_AUTH);
    }

    private void sendJsonResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setContentType("application/json"); // JSON 형식의 데이터라고 설정
        response.setCharacterEncoding("UTF-8"); // 인코딩 설정
        response.setStatus(exceptionCode.getCode());
        ErrorResponse errorResponse = new ErrorResponse(exceptionCode.getHttpStatus(), exceptionCode.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
