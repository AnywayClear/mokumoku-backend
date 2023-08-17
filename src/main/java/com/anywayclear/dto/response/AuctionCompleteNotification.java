package com.anywayclear.dto.response;


import com.anywayclear.entity.Auction;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuctionCompleteNotification {
    private final int type;
    private final String nickname;
    private final String produceName;

    @Builder
    public AuctionCompleteNotification(int type, String nickname, String produceName) {
        this.type = type;
        this.nickname = nickname;
        this.produceName = produceName;
    }

    public static AuctionCompleteNotification toResponse(Auction auction) {
        return AuctionCompleteNotification.builder()
                .type(2)
                .nickname(auction.getNickname())
                .produceName(auction.getProduce().getName())
                .build();
    }
}
