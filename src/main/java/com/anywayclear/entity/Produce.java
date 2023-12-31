package com.anywayclear.entity;

import com.anywayclear.dto.request.ProduceCreateRequest;
import com.anywayclear.util.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Produce extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member seller;

    @OneToMany(mappedBy = "produce")
    private List<Auction> auctionList = new ArrayList<>();

    @OneToMany(mappedBy = "produce")
    private List<Dib> dibList = new ArrayList<>();

    @Builder
    public Produce(String name, String description, String image, int startPrice, int kg, int ea, LocalDateTime startDate,LocalDateTime endDate, int status) {
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

    public static Produce toEntity(ProduceCreateRequest request) {
        return Produce.builder()
                .name(request.getName())
                .description(request.getDescription())
                .image(request.getImage())
                .startPrice(request.getStartPrice())
                .kg(request.getKg())
                .ea(request.getEa())
                .startDate(request.getStartDate())
                .endDate(request.getStartDate())
                .status(0)
                .build();
    }

}