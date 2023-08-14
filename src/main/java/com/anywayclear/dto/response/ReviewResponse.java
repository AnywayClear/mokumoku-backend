package com.anywayclear.dto.response;

import com.anywayclear.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private Long id;
    private String comment;
    private int score;
    private LocalDateTime createdAt;
    private DealResponse deal;
    private String reviewerId;
    private String reviewerNickname;

    @Builder
    public ReviewResponse(Long id, String comment, int score, LocalDateTime createdAt, DealResponse deal, String reviewerId, String reviewerNickname) {
        this.id = id;
        this.comment = comment;
        this.score = score;
        this.createdAt = createdAt;
        this.deal = deal;
        this.reviewerId = reviewerId;
        this.reviewerNickname = reviewerNickname;
    }

    public static ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .comment(review.getComment())
                .score(review.getScore())
                .createdAt(review.getCreatedAt())
                .deal(DealResponse.toResponse(review.getDeal()))
                .reviewerId(review.getMember().getUserId())
                .reviewerNickname(review.getMember().getNickname())
                .build();
    }
}
