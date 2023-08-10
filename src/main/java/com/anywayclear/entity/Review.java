package com.anywayclear.entity;

import com.anywayclear.dto.request.ReviewRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    @JoinColumn(name = "deal_id", referencedColumnName = "id")
    private Deal deal;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public Review(String comment, int score, Deal deal, Member member) {
        this.comment = comment;
        this.score = score;
        this.deal = deal;
        this.member = member;
    }

    public static Review toEntity(ReviewRequest request) {
        return Review.builder()
                .comment(request.getComment())
                .score(request.getScore())
                .build();
    }
}
