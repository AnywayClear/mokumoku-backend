package com.anywayclear.dto.response;

import com.anywayclear.entity.Deal;
import com.anywayclear.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealResponse {
    private int endPrice;
    private boolean isPaid;
    private Member consumer;
    private Member seller;
    private ProduceResponse produce;

    @Builder
    public DealResponse(int endPrice, boolean isPaid, Member consumer, Member seller, ProduceResponse produce) {
        this.endPrice = endPrice;
        this.isPaid = isPaid;
        this.consumer = consumer;
        this.seller = seller;
        this.produce = produce;
    }

    public static DealResponse toResponse(Deal deal) {
        return DealResponse.builder()
                .endPrice(deal.getEndPrice())
                .isPaid(deal.isPaid())
                .consumer(deal.getConsumer())
                .seller(deal.getSeller())
                .produce(ProduceResponse.toResponse(deal.getProduce()))
                .build();
    }
}
