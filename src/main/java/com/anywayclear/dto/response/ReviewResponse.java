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
    private String memberUserId;
    private String memberNickname;

    @Builder
    public ReviewResponse(Long id, String comment, int score, LocalDateTime createdAt, DealResponse deal, String memberUserId, String memberNickname) {
        this.id = id;
        this.comment = comment;
        this.score = score;
        this.createdAt = createdAt;
        this.deal = deal;
        this.memberUserId = memberUserId;
        this.memberNickname = memberNickname;
    }

    public static ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .comment(review.getComment())
                .score(review.getScore())
                .createdAt(review.getCreatedAt())
                .deal(DealResponse.toResponse(review.getDeal()))
                .memberUserId(review.getMember().getUserId())
                .memberNickname(review.getMember().getNickname())
                .build();
    }
}
