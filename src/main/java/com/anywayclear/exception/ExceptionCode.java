package com.anywayclear.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    // 200 OK : 성공
    SUCCESS(OK, "", 200),

    // 400 BAD_REQUEST : 잘못된 요청 - 비즈니스 로직 에러코드를 여기에 작성해주세요!
    INVALID_MEMBER(BAD_REQUEST, "잘못된 사용자 입니다", 400),
    INVALID_PRODUCE(BAD_REQUEST, "잘못된 농산물 정보 입니다", 400),
    INVALID_PRODUCE_ID(BAD_REQUEST, "잘못된 농산물 ID 입니다", 400),
    INVALID_AUCTION_ID(BAD_REQUEST, "잘못된 경매 ID 입니다", 400),
    INVALID_PRICE(BAD_REQUEST, "현재 입찰가보다 낮게 입찰할 수 없습니다", 400),
    EXPIRED_AUCTION_TIME(BAD_REQUEST,"경매 시간이 종료되었습니다.",400),
    INVALID_AUCTION_STATUS(BAD_REQUEST, "경매 가능한 상태가 아닙니다", 400),

    // 401 UNAUTHORIZED : 인증되지 않은 사용자
    INVALID_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다", 401),
    INVALID_EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다", 401),
    INVALID_DELETED_MEMBER(UNAUTHORIZED, "탈퇴한 회원입니다", 401),

    // 403 Forbidden : 자원에 대한 권한 없음
    INVALID_AUTH(FORBIDDEN, "권한이 없습니다", 403),

    // 404 Not Found : 요청한 URI에 대한 리소스 없음
    INVALID_RESOURCE(NOT_FOUND, "요청한 리소스가 없습니다", 404),

    // 405 Method Not Allowed : 사용 불가능한 Method 이용
    INVALID_METHOD(METHOD_NOT_ALLOWED, "지원하지 않는 Method 입니다.", 405),

    // 409 CONFLICT : 중복된 데이터 존재
    DUPLICATE_RESOURCE(CONFLICT, "", 409),

    // 500 INTERNAL_SERVER_ERROR : 서버 에러
    SERVER_ERROR(INTERNAL_SERVER_ERROR,"", 500);

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
