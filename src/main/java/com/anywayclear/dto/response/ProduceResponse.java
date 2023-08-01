package com.anywayclear.dto.response;

import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Produce;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProduceResponse {
    private String name;
    private String description;
    private String image;
    private int startPrice;
    private int kg;
    private int ea;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int status;
    private List<Auction> auctionList;

    @Builder
    public ProduceResponse(String name, String description, String image, int startPrice, int kg, int ea, LocalDateTime startDate, LocalDateTime endDate,int status,List<Auction> auctionList) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.startPrice = startPrice;
        this.kg = kg;
        this.ea = ea;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.auctionList = auctionList;
    }

    public static ProduceResponse toResponse(Produce produce) {
        return ProduceResponse.builder()
                .name(produce.getImage())
                .description(produce.getDescription())
                .image(produce.getImage())
                .startPrice(produce.getStartPrice())
                .kg(produce.getKg())
                .ea(produce.getEa())
                .startDate(produce.getStartDate())
                .endDate(produce.getEndDate())
                .status(produce.getStatus())
                .auctionList(produce.getAuctionList())
                .build();
    }
}
