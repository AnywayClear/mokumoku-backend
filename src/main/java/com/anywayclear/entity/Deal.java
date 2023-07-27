package com.anywayclear.entity;

import com.anywayclear.dto.request.DealCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private int endPrice;

    @Column(nullable = false)
    private boolean isPaid = false;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member consumer;

    @ManyToOne
    @JoinColumn(name = "id")
    @Column(nullable = false)
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "id")
    @Column(nullable = false)
    private Produce produce;

    @Builder
    public Deal(Long id, int endPrice, boolean isPaid, Member consumer, Member seller, Produce produce) {
        this.id = id;
        this.endPrice = endPrice;
        this.isPaid = isPaid;
        this.consumer = consumer;
        this.seller = seller;
        this.produce = produce;
    }

    public static Deal toEntity(DealCreateRequest request) {
        return Deal.builder()
                .endPrice(request.getEndPrice())
                .isPaid(request.isIspaid())
                .consumer(request.getConsumer())
                .seller(request.getSeller())
                .produce(request.getProduce())
                .build();
    }

}
