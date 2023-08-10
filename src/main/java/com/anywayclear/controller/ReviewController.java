package com.anywayclear.controller;

import com.anywayclear.dto.request.ReviewRequest;
import com.anywayclear.dto.response.ReviewResponse;
import com.anywayclear.service.ReviewService;
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
        reviewService.getReview(dealId);
        return ResponseEntity.ok(reviewService.getReview(dealId));
    }

    @PostMapping("/{dealId}")
    public ResponseEntity<Long> createReview(
            @RequestBody ReviewRequest request,
            @PathVariable("dealId") Long dealId,
            @AuthenticationPrincipal OAuth2User oAuth2User){
        String reviewerId = (String) oAuth2User.getAttributes().get("userId");
        return ResponseEntity.ok(reviewService.createReview(reviewerId, dealId, request));
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
