package com.anywayclear.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produce_id")
    private Produce produce;

    private int bidding;

    public Auction(Produce produce) {
        this.produce = produce;
        this.bidding = produce.getStartPrice();
    }
}
