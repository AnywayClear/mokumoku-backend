package com.anywayclear.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BiddingResponse {
    private final String userId;
    private final String nickname;
    private final LocalDateTime updatedAt;
    private final int price;

    @Builder
    public BiddingResponse(String userId, String nickname, LocalDateTime updatedAt, int price) {
        this.userId = userId;
        this.nickname = nickname;
        this.updatedAt = updatedAt;
        this.price = price;
    }

    public static BiddingResponse toResponse(String userId, int price) {
        return BiddingResponse.builder()
                .userId(userId)
                .price(price)
                .build();
    }
}
