package com.anywayclear.dto.response;

import com.anywayclear.entity.Auction;
import com.anywayclear.entity.Member;
import com.anywayclear.entity.Review;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ReviewResponse {

    private Long id;
    private String comment;
    private int score;
    private String createdDate;
    private Auction auction;
    private String memberUserId;
    private String memberNickname;

    @Builder
    public ReviewResponse(Long id, String comment, int score, String createdDate, Auction auction, String memberUserId, String memberNickname) {
        this.id = id;
        this.comment = comment;
        this.score = score;
        this.createdDate = createdDate;
        this.auction = auction;
        this.memberUserId = memberUserId;
        this.memberNickname = memberNickname;
    }

    public static ReviewResponse toResponse(Review review) {
        ReviewResponse rr = ReviewResponse.builder()
                .id(review.getId())
                .comment(review.getComment())
                .score(review.getScore())
                .createdDate(review.getCreatedDate())
                .auction(review.getAuction())
                .memberUserId(review.getMember().getUserId())
                .memberNickname(review.getMember().getNickname())
                .build();

        System.out.println("rr = " + rr);
        return rr;
    }
}
