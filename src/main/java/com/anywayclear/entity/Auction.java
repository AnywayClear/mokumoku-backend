package com.anywayclear.entity;

import com.anywayclear.util.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auction extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produce_id")
    private Produce produce;

    private int price;

    private String nickname;
    private LocalDateTime lastBidding;

    private boolean isClosed;
    public Auction(Produce produce) {
        this.produce = produce;
        this.price = produce.getStartPrice();
        this.nickname = "";
        this.lastBidding = produce.getStartDate();
        this.isClosed = false;
    }
}
