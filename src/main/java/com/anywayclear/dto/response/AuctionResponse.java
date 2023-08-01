package com.anywayclear.dto.response;

import com.anywayclear.entity.Auction;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuctionResponse {
    private final Long id;
    private final int price;

    @Builder
    public AuctionResponse(Long id, int price) {
        this.id = id;
        this.price = price;
    }

    public static AuctionResponse toResponse(Auction auction) {
        return AuctionResponse.builder()
                .id(auction.getId())
                .price(auction.getPrice())
                .build();
    }
}
