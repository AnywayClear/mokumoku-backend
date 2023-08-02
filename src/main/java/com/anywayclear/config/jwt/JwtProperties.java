package com.anywayclear.config.jwt;

public interface JwtProperties {
    String SECRET_KEY = "kyunwangJwt";
    int EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
