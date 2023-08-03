package com.anywayclear.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    // 200 OK : 성공
    SUCCESS(OK, ""),

    // 400 BAD_REQUEST : 잘못된 요청 - 비즈니스 로직 에러코드를 여기에 작성해주세요!
    INVALID_MEMBER(BAD_REQUEST, "잘못된 사용자 입니다"),
    INVALID_PRODUCE(BAD_REQUEST, "잘못된 농산물 정보 입니다"),
    INVALID_PRODUCE_ID(BAD_REQUEST, "잘못된 농산물 ID 입니다"),
    INVALID_AUCTION_ID(BAD_REQUEST, "잘못된 경매 ID 입니다"),
    INVALID_PRICE(BAD_REQUEST, "현재 입찰가보다 낮게 입찰할 수 없습니다"),

    // 401 UNAUTHORIZED : 인증되지 않은 사용자
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),

    // 403 Forbidden : 자원에 대한 권한 없음
    INVALID_AUTH(FORBIDDEN, "권한이 없습니다"),

    // 404 Not Found : 요청한 URI에 대한 리소스 없음
    INVALID_RESOURCE(NOT_FOUND, "요청한 리소스가 없습니다"),

    // 405 Method Not Allowed : 사용 불가능한 Method 이용
    INVALID_METHOD(METHOD_NOT_ALLOWED, "지원하지 않는 Method 입니다."),

    // 409 CONFLICT : 중복된 데이터 존재
    DUPLICATE_RESOURCE(CONFLICT, ""),

    // 500 INTERNAL_SERVER_ERROR : 서버 에러
    SERVER_ERROR(INTERNAL_SERVER_ERROR,"");

    private final HttpStatus httpStatus;
    private final String message;
}
