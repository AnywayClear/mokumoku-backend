package com.anywayclear.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BiddingRequest {
    private String userId;
    private int price;
}
