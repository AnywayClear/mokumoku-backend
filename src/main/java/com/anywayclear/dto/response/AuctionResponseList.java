package com.anywayclear.dto.response;

import com.anywayclear.entity.Auction;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AuctionResponseList {
    private final List<AuctionResponse> auctionResponseList;

    public AuctionResponseList(final List<Auction> auctionList) {
        this.auctionResponseList = auctionList.stream()
                .map(AuctionResponse::toResponse)
                .collect(Collectors.toList());
    }
}
