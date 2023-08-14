package com.anywayclear.controller;

import com.anywayclear.dto.request.ReviewRequest;
import com.anywayclear.dto.response.MultiResponse;
import com.anywayclear.dto.response.ReviewResponse;
import com.anywayclear.entity.Review;
import com.anywayclear.service.ReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{dealId}")
    public ResponseEntity<ReviewResponse> getReview(
            @PathVariable("dealId") Long dealId) {
        return ResponseEntity.ok(reviewService.getReview(dealId));
    }

    @GetMapping("/lists/{userId}")
    public ResponseEntity<MultiResponse<ReviewResponse, Review>> getReviewList(
            @PathVariable("userId") String userId,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sorted", required = false) String sortedBy,
            Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.getReviewList(userId, pageable, q, sortedBy));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Long> updateReview(
            @RequestBody ReviewRequest request,
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal OAuth2User oAuth2User) {
        String reviewerId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(reviewService.updateReview(request,reviewId, reviewerId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Long> deleteReview(
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal OAuth2User oAuth2User) {
        String reviewerId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(reviewService.deleteReview(reviewId, reviewerId));
    }
}
