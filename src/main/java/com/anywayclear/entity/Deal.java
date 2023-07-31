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
    @JoinColumn(name = "consumer_id", referencedColumnName = "id")
    private Member consumer;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "produce_id", referencedColumnName = "id")
    private Produce produce;

    @Builder
    public Deal(int endPrice, Member consumer, Member seller, Produce produce) {
        this.endPrice = endPrice;
        this.consumer = consumer;
        this.seller = seller;
        this.produce = produce;
    }

    public static Deal toEntity(DealCreateRequest request) {
        return Deal.builder()
                .endPrice(request.getEndPrice())
                .consumer(request.getConsumer())
                .seller(request.getSeller())
                .produce(request.getProduce())
                .build();
    }

}
