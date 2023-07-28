package com.anywayclear.dto.request;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class DealCreateRequest {

    @NotBlank
    private int endPrice;
    @NotBlank
    private boolean ispaid;
    @NotBlank
    private Member consumer;
    @NotBlank
    private Member seller;
    @NotBlank
    private Produce produce;

    @Builder
    public DealCreateRequest(int endPrice, boolean ispaid, Member consumer, Member seller, Produce produce) {
        this.endPrice = endPrice;
        this.ispaid = ispaid;
        this.consumer = consumer;
        this.seller = seller;
        this.produce = produce;
    }
}
