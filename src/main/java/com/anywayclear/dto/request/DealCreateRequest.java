package com.anywayclear.dto.request;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DealCreateRequest {

    @NotNull
    private int endPrice;
    @NotNull
    private Member consumer;
    @NotNull
    private Member seller;
    @NotNull
    private Produce produce;

    @Builder
    public DealCreateRequest(int endPrice, Member consumer, Member seller, Produce produce) {
        this.endPrice = endPrice;
        this.consumer = consumer;
        this.seller = seller;
        this.produce = produce;
    }
}
