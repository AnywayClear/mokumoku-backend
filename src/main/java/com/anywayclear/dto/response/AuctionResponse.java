package com.anywayclear.dto.response;

import com.anywayclear.entity.Auction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionResponse {
    private final Long id;
    private final int price;
    private final String nickname;
    private final LocalDateTime updatedAt;
    private final boolean status;

    @Builder
    public AuctionResponse(Long id, int price, String nickname, LocalDateTime updatedAt, boolean status) {
        this.id = id;
        this.price = price;
        this.nickname = nickname;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public static AuctionResponse toResponse(Auction auction) {
        return AuctionResponse.builder()
                .id(auction.getId())
                .price(auction.getPrice())
                .nickname(auction.getNickname())
                .updatedAt(auction.getUpdatedAt())
                .status(auction.isClosed())
                .build();
    }
}
