package com.anywayclear.dto.response;

import com.anywayclear.entity.Produce;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Builder
    public ProduceResponse(String name, String description, String image, int startPrice, int kg, int ea, LocalDateTime startDate, LocalDateTime endDate,int status) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.startPrice = startPrice;
        this.kg = kg;
        this.ea = ea;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
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
                .build();
    }
}
