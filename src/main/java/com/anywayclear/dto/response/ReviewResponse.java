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
    private LocalDateTime createdAt;
    private AuctionResponse auction;
    private String memberUserId;
    private String memberNickname;

    @Builder
    public ReviewResponse(Long id, String comment, int score, LocalDateTime createdAt,String createdDate, AuctionResponse auction, String memberUserId, String memberNickname) {
        this.id = id;
        this.comment = comment;
        this.score = score;
        this.createdAt = createdAt;
        this.auction = auction;
        this.memberUserId = memberUserId;
        this.memberNickname = memberNickname;
    }

    public static ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .comment(review.getComment())
                .score(review.getScore())
                .createdAt(review.getCreatedAt())
                .auction(AuctionResponse.toResponse(review.getAuction()))
                .memberUserId(review.getMember().getUserId())
                .memberNickname(review.getMember().getNickname())
                .build();
    }
}
