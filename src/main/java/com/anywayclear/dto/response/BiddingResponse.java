package com.anywayclear.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BiddingResponse {
    private String userId;
    private int price;

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
