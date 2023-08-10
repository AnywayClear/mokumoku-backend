package com.anywayclear.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auction extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produce_id")
    private Produce produce;

    @OneToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private Review review;

    private int price;

    private String nickname;

    private int status; // 0: 대기, 1: 경매중, 2: 경매 완료
    public Auction(Produce produce) {
        this.produce = produce;
        this.price = produce.getStartPrice();
        this.nickname = "";
    }
}
