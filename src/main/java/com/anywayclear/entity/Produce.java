package com.anywayclear.entity;

import com.anywayclear.dto.request.ProduceCreateRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Produce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String desc;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int startPrice;

    @Column(nullable = false)
    private int kg;

    @Column(nullable = false)
    private int ea;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int status;

    @Builder
    public Produce(String name, String desc, String image, int startPrice, int kg, int ea, LocalDateTime startDate, LocalDateTime endDate, int status) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.startPrice = startPrice;
        this.kg = kg;
        this.ea = ea;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public static Produce toEntity(ProduceCreateRequest request) {
        return Produce.builder()
                .name(request.getName())
                .desc(request.getDesc())
                .image(request.getImage())
                .startPrice(request.getStartPrice())
                .kg(request.getKg())
                .ea(request.getEa())
                .startPrice(request.getStartPrice())
                .endDate(request.getEndDate())
                .status(0)
                .build();
    }

}