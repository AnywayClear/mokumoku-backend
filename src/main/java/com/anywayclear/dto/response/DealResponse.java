package com.anywayclear.dto.response;

import com.anywayclear.entity.Deal;
import com.anywayclear.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealResponse {
    private long dealId;
    private int endPrice;
    private boolean isPaid;
    private Member consumer;
    private Member seller;
    private ProduceResponse produce;
    private boolean isReviewed;

    @Builder
    public DealResponse(long dealId,int endPrice, boolean isPaid, Member consumer, Member seller, ProduceResponse produce, boolean isReviewed) {
        this.dealId = dealId;
        this.endPrice = endPrice;
        this.isPaid = isPaid;
        this.consumer = consumer;
        this.seller = seller;
        this.produce = produce;
        this.isReviewed = isReviewed;
    }

    public static DealResponse toResponse(Deal deal) {
        return DealResponse.builder()
                .dealId(deal.getId())
                .endPrice(deal.getEndPrice())
                .isPaid(deal.isPaid())
                .consumer(deal.getConsumer())
                .seller(deal.getSeller())
                .produce(ProduceResponse.toResponse(deal.getProduce()))
                .isReviewed((deal.getReview() != null) ? true : false)
                .build();
    }
}
