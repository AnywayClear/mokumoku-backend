//package com.anywayclear.entity;
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Bidding {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "auction_id")
//    private Auction auction;
//
//    private String nickname;
//
//    private int price;
//
//    public Bidding(Auction auction) {
//        this.auction = auction;
//        this
//    }
//}
