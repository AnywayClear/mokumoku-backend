package com.anywayclear.dto.response;

import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Produce;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
//@Setter
public class ProduceResponse {
    private final String name;
    private final String description;
    private final String image;
    private final int startPrice;
    private final int kg;
    private final int ea;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final int status;
    private final List<AuctionResponse> auctionResponseList;

    @Builder
    public ProduceResponse(String name, String description, String image, int startPrice, int kg, int ea, LocalDateTime startDate, LocalDateTime endDate,int status,AuctionResponseList auctionResponseList) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.startPrice = startPrice;
        this.kg = kg;
        this.ea = ea;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.auctionResponseList = auctionResponseList.getAuctionResponseList();
    }

    public static ProduceResponse toResponse(Produce produce) {
        return ProduceResponse.builder()
                .name(produce.getName())
                .description(produce.getDescription())
                .image(produce.getImage())
                .startPrice(produce.getStartPrice())
                .kg(produce.getKg())
                .ea(produce.getEa())
                .startDate(produce.getStartDate())
                .endDate(produce.getEndDate())
                .status(produce.getStatus())
                .auctionResponseList(new AuctionResponseList(produce.getAuctionList()))
                .build();
    }
}
