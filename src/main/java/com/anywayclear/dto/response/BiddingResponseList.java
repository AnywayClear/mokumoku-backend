package com.anywayclear.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class BiddingResponseList {
    private final List<BiddingResponse> biddingResponseList;

    public BiddingResponseList(List<BiddingResponse> biddingResponseList) {
        this.biddingResponseList = biddingResponseList;
    }
}
