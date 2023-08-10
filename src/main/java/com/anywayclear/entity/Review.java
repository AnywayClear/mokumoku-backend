package com.anywayclear.entity;

import com.anywayclear.dto.request.ReviewCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Review extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int score;

    @OneToOne
    @JoinColumn(name = "auction_id", referencedColumnName = "id")
    private Auction auction;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public Review(String comment, int score, Auction auction, Member member) {
        this.comment = comment;
        this.score = score;
        this.auction = auction;
        this.member = member;
    }

    public static Review toEntity(ReviewCreateRequest request) {
        return Review.builder()
                .comment(request.getComment())
                .score(request.getScore())
                .build();
    }
}
