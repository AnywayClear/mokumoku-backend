package com.anywayclear.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BiddingResponse {
    private final String userId;
    private final int price;

    @Builder
    public BiddingResponse(String userId, int price) {
        this.userId = userId;
        this.price = price;
    }

    public static BiddingResponse toResponse(String userId, int price) {
        return BiddingResponse.builder()
                .userId(userId)
                .price(price)
                .build();
    }
}
