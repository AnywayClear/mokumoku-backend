package com.anywayclear.dto.response;

import com.anywayclear.entity.Dib;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Produce;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
//@Setter
@ToString
public class ProduceResponse {
    private final long id;
    private final String name;
    private final String seller;
    private final String sellerId;
    private final String description;
    private final String image;
    private final int startPrice;
    private final int kg;
    private final int ea;
    private final String sellerImage;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final int status;
    private final List<AuctionResponse> auctionResponseList;
    private final int dibNum;

    @Builder
    public ProduceResponse(long id, String name, Member member, String description, String image, int startPrice, int kg, int ea, String sellerImage, LocalDateTime startDate, LocalDateTime endDate, int status, AuctionResponseList auctionResponseList, List<Dib> dibList) {
        this.id = id;
        this.name = name;
        this.seller = member.getNickname();
        this.sellerId = member.getUserId();
//        this.seller = "testSeller";
        this.description = description;
        this.image = image;
        this.startPrice = startPrice;
        this.kg = kg;
        this.ea = ea;
        this.sellerImage = sellerImage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.auctionResponseList = auctionResponseList.getAuctionResponseList();
        this.dibNum = dibList.size();
    }

    public static ProduceResponse toResponse(Produce produce) {
        return ProduceResponse.builder()
                .id(produce.getId())
                .name(produce.getName())
                .member(produce.getSeller())
                .sellerImage(produce.getSeller().getImage())
//                .member(new Member())
                .description(produce.getDescription())
                .image(produce.getImage())
                .startPrice(produce.getStartPrice())
                .kg(produce.getKg())
                .ea(produce.getEa())
                .startDate(produce.getStartDate())
                .endDate(produce.getEndDate())
                .status(produce.getStatus())
                .auctionResponseList(new AuctionResponseList(produce.getAuctionList()))
                .dibList(produce.getDibList())
                .build();
    }
}
