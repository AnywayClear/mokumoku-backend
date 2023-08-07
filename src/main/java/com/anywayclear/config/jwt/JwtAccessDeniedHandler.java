package com.anywayclear.config.jwt;

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
        String errorMessage  = accessDeniedException.getMessage();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        sendJsonResponse(response, errorMessage);
    }

    private void sendJsonResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json"); // JSON 형식의 데이터라고 설정
        response.setCharacterEncoding("UTF-8"); // 인코딩 설정

        // JSON 데이터를 생성
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("httpStatus", String.valueOf(response.getStatus()));
        jsonMap.put("error", errorMessage);
        String json = objectMapper.writeValueAsString(jsonMap);

        // JSON 데이터를 클라이언트로 전송
        response.getWriter().write(json);
    }
}
